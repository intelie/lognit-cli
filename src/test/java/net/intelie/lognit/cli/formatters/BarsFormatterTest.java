package net.intelie.lognit.cli.formatters;

import com.google.common.collect.Lists;
import jline.ANSIBuffer;
import net.intelie.lognit.cli.model.FreqPoint;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class BarsFormatterTest {
    @Test
    public void whenRepresentingFields() throws Exception {
        BarsFormatter bars = new BarsFormatter();

        List<FreqPoint<String>> points = Lists.newArrayList();
        points.add(new FreqPoint<String>("A", 11111));
        points.add(new FreqPoint<String>("B", 22222));
        points.add(new FreqPoint<String>("CCCCCCCCCCCCCCCCCCCC", 33333));
        points.add(new FreqPoint<String>(null, 44444));
        points.add(new FreqPoint<String>("E", 55555));

        List<String> data = bars.field("bla", points, false);

        assertThat(data).containsExactly(
                "Top 10 blas (last 24h):                ",
                "A                    11,111 ■■         ",
                "B                    22,222 ■■■■       ",
                "CCCCCCCCCCCCCCC…     33,333 ■■■■■■     ",
                "null                 44,444 ■■■■■■■■   ",
                "E                    55,555 ■■■■■■■■■■ "
        );

    }

    @Test
    public void whenRepresentingHours() throws Exception {
        BarsFormatter bars = new BarsFormatter();

        List<FreqPoint<Long>> points = Lists.newArrayList();
        points.add(new FreqPoint<Long>(defaultTime(22, 0), 11111));
        points.add(new FreqPoint<Long>(defaultTime(23, 0), 22222));
        points.add(new FreqPoint<Long>(defaultTime(24 + 0, 0), 33333));
        points.add(new FreqPoint<Long>(defaultTime(24 + 1, 0), 44444));
        points.add(new FreqPoint<Long>(defaultTime(24 + 2, 0), 55555));

        List<String> data = bars.hours(points, false);

        assertThat(data).containsExactly(
                "24h (1980-05-21, 1980-05-22)           ",
                "22h     11,111 ■■■■                    ",
                "23h     22,222 ■■■■■■■■                ",
                "00h     33,333 ■■■■■■■■■■■■            ",
                "01h     44,444 ■■■■■■■■■■■■■■■■        ",
                "02h     55,555 ■■■■■■■■■■■■■■■■■■■■    "
        );

    }

    @Test
    public void whenRepresentingLastHours() throws Exception {
        BarsFormatter bars = new BarsFormatter();

        List<FreqPoint<Long>> points = Lists.newArrayList();
        points.add(new FreqPoint<Long>(defaultTime(22, 1), 11111));
        points.add(new FreqPoint<Long>(defaultTime(22, 2), 22222));
        points.add(new FreqPoint<Long>(defaultTime(22, 3), 33333));
        points.add(new FreqPoint<Long>(defaultTime(22, 4), 44444));
        points.add(new FreqPoint<Long>(defaultTime(22, 5), 55555));

        List<String> data = bars.lastHour(points, false);

        assertThat(data).containsExactly(
                "This hour:                             ",
                "22:01      11,111 ■■■■                 ",
                "22:02      22,222 ■■■■■■■■             ",
                "22:03      33,333 ■■■■■■■■■■■■         ",
                "22:04      44,444 ■■■■■■■■■■■■■■■■     ",
                "22:05      55,555 ■■■■■■■■■■■■■■■■■■■■ "
        );

    }


    private static final String YELLOW = "\033[33m";
    private static final String GREEN = "\033[32m";
    private static final String CYAN = "\033[36m";
    private static final String NONE = "\033[0m";

    @Test
    public void makeLineWillRespectOnlyVisiblePartAndCreateColumnsExactly() throws Exception {
        assertThat(BarsFormatter.makeLine(true, new ANSIBuffer().append("abc"), 4)).isEqualTo("abc ");
        assertThat(BarsFormatter.makeLine(true, new ANSIBuffer().yellow("abc"), 4)).isEqualTo(colored("$yabc$n "));
        assertThat(BarsFormatter.makeLine(false, new ANSIBuffer().yellow("abc"), 4)).isEqualTo("abc ");
    }

    @Test
    public void creatingColoredBarsWithEqualReference() throws Exception {
        List<FreqPoint<Long>> points = Lists.newArrayList();
        points.add(new FreqPoint<Long>(defaultTime(22, 1), 22222));
        points.add(new FreqPoint<Long>(defaultTime(22, 2), 22222));

        ANSIBuffer buffer = new ANSIBuffer();
        BarsFormatter.drawBar(buffer, points, points.get(1), 10);

        assertThat(buffer.toString()).isEqualTo(testBar(10, 0, 0));
    }

    @Test
    public void creatingColoredBarsWithDifferentReferences() throws Exception {
        List<FreqPoint<Long>> points = Lists.newArrayList();
        points.add(new FreqPoint<Long>(defaultTime(22, 1), 30));
        points.add(new FreqPoint<Long>(defaultTime(22, 2), 20));
        points.add(new FreqPoint<Long>(defaultTime(22, 3), 10));

        ANSIBuffer buffer = new ANSIBuffer();
        BarsFormatter.drawBar(buffer, points, points.get(0), 10);

        assertThat(buffer.toString()).isEqualTo(testBar(5, 2, 3));
    }

    @Test
    public void barsMinimumValueIs10() throws Exception {
        List<FreqPoint<Long>> points = Lists.newArrayList();
        points.add(new FreqPoint<Long>(defaultTime(22, 1), 7));
        points.add(new FreqPoint<Long>(defaultTime(22, 2), 7));
        points.add(new FreqPoint<Long>(defaultTime(22, 3), 7));

        ANSIBuffer buffer = new ANSIBuffer();
        BarsFormatter.drawBar(buffer, points, points.get(2), 10);

        assertThat(buffer.toString()).isEqualTo(testBar(7, 0, 0));
    }


    private String testBar(int green, int yellow, int red) {
        ANSIBuffer buffer = new ANSIBuffer();
        for (int i = 0; i < green; i++)
            buffer.green("■");
        for (int i = 0; i < yellow; i++)
            buffer.yellow("■");
        for (int i = 0; i < red; i++)
            buffer.red("■");
        return buffer.toString();
    }

    private String colored(String s) {
        return s.replace("$c", CYAN).replace("$g", GREEN).replace("$y", YELLOW).replace("$n", NONE);
    }

    private String nonColored(String s) {
        return s.replace("$c", "").replace("$g", "").replace("$y", "").replace("$n", "");
    }


    public long defaultTime(int hours, int minutes) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(1980, 4, 21, 0, 0, 0);
        return calendar.getTimeInMillis() + hours * 60 * 60 * 1000 + minutes * 60 * 1000;
    }
}
