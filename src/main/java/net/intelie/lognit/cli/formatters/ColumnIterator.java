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

        long max = calculateMaxFreq(points);
        double avg = calculateAvgFreq(points);
        double stdDev = calculateStdev(points, avg);

        list.add(new ANSIBuffer().cyan("This hour:").toString(realColor));
        for (FreqPoint<Long> point : points) {
            ANSIBuffer buffer = new ANSIBuffer();
            buffer.append(String.format("%tR %,10d ", point.key(), point.freq()));
            makeBar(buffer, point, 16, max, avg, stdDev);
            list.add(buffer.toString(realColor));
        }
        return list;
    }

    public static List<String> reprHours(List<FreqPoint<Long>> points, boolean realColor) {
        List<String> list = Lists.newArrayList();

        long max = calculateMaxFreq(points);
        double avg = calculateAvgFreq(points);
        double stdDev = calculateStdev(points, avg);

        list.add(makeString(realColor, new ANSIBuffer().cyan(String.format(
                "24h (%tF, %tF)", points.get(0).key(), points.get(points.size() - 1).key())), 33));
        for (FreqPoint<Long> point : points) {
            ANSIBuffer buffer = new ANSIBuffer();
            buffer.append(String.format("%tHh %,10d ", point.key(), point.freq()));
            makeBar(buffer, point, 16, max, avg, stdDev);
            list.add(makeString(realColor, buffer, 33));
        }

        return list;
    }

    public static List<String> reprField(String field, List<FreqPoint<String>> points, boolean realColor) {
        List<String> list = Lists.newArrayList();

        long max = calculateMaxFreq(points);
        double avg = calculateAvgFreq(points);
        double stdDev = calculateStdev(points, avg);

        list.add(new ANSIBuffer().cyan("Top 10 " + field + "s (last 24h):").toString(realColor));
        for (FreqPoint<String> point : points) {
            ANSIBuffer buffer = new ANSIBuffer();

            String host = point.key();
            if (host.length() > 16)
                host = host.substring(0, 15) + "…";

            buffer.append(String.format("%-16.16s %,10d ", host, point.freq()));
            makeBar(buffer, point, 16, max, avg, stdDev);
            list.add(buffer.toString(realColor));
        }

        return list;
    }

    private static String makeString(boolean realColor, ANSIBuffer buffer, int width) {
        String nonAnsi = buffer.getPlainBuffer();
        return buffer.toString(realColor) + Strings.repeat(" ", Math.max(0, width - nonAnsi.length()));
    }

    private static void makeBar(ANSIBuffer buffer, FreqPoint point, int maxBars, long max, double avg, double stdev) {
        int bars = (int) Math.ceil(maxBars * point.freq() / (double) max);
        int std1 = (int) Math.ceil(maxBars * (avg + stdev) / (double) max);
        int std2 = (int) Math.ceil(maxBars * (avg + 2 * stdev) / (double) max);
        for (int i = 0; i < bars; i++) {
            if (i < std1)
                buffer.green(BAR_CHAR);
            else if (i < std2)
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

    private static <T> double calculateAvgFreq(List<FreqPoint<T>> points) {
        double avg = 0;
        for (FreqPoint point : points)
            avg += point.freq();
        return avg / points.size();
    }


    private static <T> double calculateStdev(List<FreqPoint<T>> points, double avg) {
        double stdev = 0;
        for (FreqPoint point : points)
            stdev += Math.pow(point.freq() - avg, 2) / points.size();
        return Math.sqrt(stdev);
    }
}
