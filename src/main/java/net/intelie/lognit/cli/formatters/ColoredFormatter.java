package net.intelie.lognit.cli.formatters;

import com.google.common.base.Joiner;
import jline.ANSIBuffer;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.FreqPoint;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.SearchStats;

import java.util.*;

public class ColoredFormatter implements Formatter {
    private final UserConsole console;
    private final BarsFormatter bars;
    private boolean colored;

    public ColoredFormatter(UserConsole console) {
        this(console, true);
    }


    public ColoredFormatter(UserConsole console, boolean colored) {
        this(console, new BarsFormatter(), colored);
    }


    public ColoredFormatter(UserConsole console, BarsFormatter bars, boolean colored) {
        this.console = console;
        this.bars = bars;
        this.colored = colored;
    }

    public ColoredFormatter(UserConsole console, BarsFormatter bars) {
        this(console, bars, true);
    }

    @Override
    public void printStatus(String format, Object... args) {
        console.println(format, args);
    }

    @Override
    public void print(Message message) {
        ANSIBuffer buffer = new ANSIBuffer();
        buffer.cyan(message.getHost());
        buffer.append(" ");
        buffer.green(message.formattedDateTime());
        buffer.append(" ");
        buffer.append(message.getFacility());
        buffer.append(" ");
        buffer.append(message.getSeverity());
        buffer.append(" ");
        buffer.yellow(message.getApp());
        buffer.append(" ");
        buffer.append(message.getMessage());

        Map<String, List<String>> metadata = message.getMetadata();
        if (metadata != null) {
            for (Map.Entry<String, List<String>> entry : metadata.entrySet()) {
                buffer.append(" ");
                buffer.cyan(entry.getKey() + ":");
                buffer.append(joinValues(entry.getValue()));
            }
        }

        console.printOut(buffer.toString(reallyColored()));
    }

    private String joinValues(List<String> value) {
        return Joiner.on(new ANSIBuffer()
                .cyan(",")
                .toString(reallyColored())).join(value);
    }

    private boolean reallyColored() {
        return colored && console.isTTY();
    }

    @Override
    public void print(Aggregated aggregated) {
        for (LinkedHashMap<String, Object> map : aggregated) {
            ANSIBuffer buffer = new ANSIBuffer();
            int count = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (count++ > 0)
                    buffer.append(" ");
                buffer.append(entry.getKey());
                buffer.append(":");
                buffer.green("" + entry.getValue());
            }
            console.printOut(buffer.toString(reallyColored()));
        }

    }

    @Override
    public void print(SearchStats stats) {
        Queue<String> left = new ArrayDeque<String>();
        Queue<String> right = new ArrayDeque<String>();

        left.addAll(bars.hours(stats.hours(), reallyColored()));
        right.addAll(bars.lastHour(stats.last(), reallyColored()));

        for (Map.Entry<String, List<FreqPoint<String>>> entry : stats.fields().entrySet()) {
            Queue<String> current = left.size() <= right.size() ? left : right;

            current.add("");
            current.addAll(bars.field(entry.getKey(), entry.getValue(), reallyColored()));
        }


        while (!left.isEmpty() || !right.isEmpty()) {
            String cLeft = left.poll();
            String cRight = right.poll();
            console.printOut("%s%s", cLeft != null ? cLeft : "", cRight != null ? cRight : "");
        }

    }


}
