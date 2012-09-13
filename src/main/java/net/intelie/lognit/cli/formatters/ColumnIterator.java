package net.intelie.lognit.cli.formatters;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import jline.ANSIBuffer;
import net.intelie.lognit.cli.model.FreqPoint;

import java.util.List;

public class ColumnIterator {
    public static final String BAR_CHAR = "\u25A0";

    public static List<String> reprLastHour(List<FreqPoint<Long>> points, boolean realColor) {
        List<String> list = Lists.newArrayList();

        long maxFreq = calculateMaxFreq(points);

        list.add(new ANSIBuffer().cyan("This hour:").toString(realColor));
        for (FreqPoint<Long> point : points) {
            ANSIBuffer buffer = new ANSIBuffer();
            buffer.append(String.format("%tR %,10d ", point.key(), point.freq()));
            makeBar(buffer, point, maxFreq, 16);
            list.add(buffer.toString(realColor));
        }
        return list;
    }

    public static List<String> reprHours(List<FreqPoint<Long>> points, boolean realColor) {
        List<String> list = Lists.newArrayList();

        long maxFreq = calculateMaxFreq(points);

        list.add(makeString(realColor, new ANSIBuffer().cyan(String.format(
                "24h (%tF, %tF)", points.get(0).key(), points.get(points.size() - 1).key())), 33));
        for (FreqPoint<Long> point : points) {
            ANSIBuffer buffer = new ANSIBuffer();
            buffer.append(String.format("%tHh %,10d ", point.key(), point.freq()));
            makeBar(buffer, point, maxFreq, 16);
            list.add(makeString(realColor, buffer, 33));
        }

        return list;
    }

    public static List<String> reprField(String field, List<FreqPoint<String>> points, boolean realColor) {
        List<String> list = Lists.newArrayList();

        long maxFreq = calculateMaxFreq(points);

        list.add(new ANSIBuffer().cyan("Top 10 " + field + "s (last 24h):").toString(realColor));
        for (FreqPoint<String> point : points) {
            ANSIBuffer buffer = new ANSIBuffer();
            buffer.append(String.format("%-16.16s %,10d ", point.key(), point.freq()));
            makeBar(buffer, point, maxFreq, 16);
            list.add(buffer.toString(realColor));
        }

        return list;
    }

    private static String makeString(boolean realColor, ANSIBuffer buffer, int width) {
        String nonAnsi = buffer.getPlainBuffer();
        return buffer.toString(realColor) + Strings.repeat(" ", Math.max(0, width - nonAnsi.length()));
    }

    private static void makeBar(ANSIBuffer buffer, FreqPoint point, long maxFreq, int maxBars) {
        int bars = (int) Math.ceil(maxBars * point.freq() / (double) maxFreq);
        for (int i = 0; i < bars; i++) {
            if (i < maxBars / 2)
                buffer.green(BAR_CHAR);
            else if (i < 7 * maxBars / 8)
                buffer.yellow(BAR_CHAR);
            else
                buffer.red(BAR_CHAR);
        }
    }

    private static <T> long calculateMaxFreq(List<FreqPoint<T>> points) {
        long max = 10;
        for (FreqPoint point : points)
            max = Math.max(max, point.freq());
        return max;
    }

}
