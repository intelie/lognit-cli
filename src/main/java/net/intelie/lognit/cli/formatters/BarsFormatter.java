package net.intelie.lognit.cli.formatters;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import jline.ANSIBuffer;
import net.intelie.lognit.cli.model.FreqPoint;

import java.util.List;
import java.util.Locale;

public class BarsFormatter {
    public static final String BAR_CHAR = "\u25A0";
    public static final String ELLIPSIS = "…";

    public List<String> lastHour(List<FreqPoint<Long>> points, boolean colored) {
        List<String> list = Lists.newArrayList();

        list.add(makeLine(colored, new ANSIBuffer().cyan("This hour:")));
        for (FreqPoint<Long> point : points) {
            ANSIBuffer buffer = new ANSIBuffer();
            buffer.append(String.format(Locale.US,"%tR  %,10d ", point.key(), point.freq()));
            drawBar(buffer, points, point, 20);
            list.add(makeLine(colored, buffer));
        }
        return list;
    }

    public List<String> hours(List<FreqPoint<Long>> points, boolean colored) {
        List<String> list = Lists.newArrayList();

        if (points.size() > 0)
            list.add(makeLine(colored, new ANSIBuffer().cyan(String.format(Locale.US,
                    "24h (%tF, %tF)", points.get(0).key(), points.get(points.size() - 1).key()))));
        else
            list.add(makeLine(colored, new ANSIBuffer().cyan(String.format(Locale.US,"24h"))));

        for (FreqPoint<Long> point : points) {
            ANSIBuffer buffer = new ANSIBuffer();
            buffer.append(String.format(Locale.US,"%tHh %,10d ", point.key(), point.freq()));
            drawBar(buffer, points, point, 20);
            list.add(makeLine(colored, buffer));
        }

        return list;
    }

    public List<String> field(String field, List<FreqPoint<String>> points, boolean colored) {
        List<String> list = Lists.newArrayList();

        list.add(makeLine(colored, new ANSIBuffer().cyan("Top 10 " + field + "s (last 24h):")));
        for (FreqPoint<String> point : points) {
            ANSIBuffer buffer = new ANSIBuffer();

            String host = point.key();
            if (host != null && host.length() > 16)
                host = host.substring(0, 15) + ELLIPSIS;

            buffer.append(String.format(Locale.US,"%-16.16s %,10d ", host, point.freq()));
            drawBar(buffer, points, point, 10);
            list.add(makeLine(colored, buffer));
        }

        return list;
    }

    public static String makeLine(boolean realColor, ANSIBuffer buffer) {
        return makeLine(realColor, buffer, 39);
    }

    public static String makeLine(boolean realColor, ANSIBuffer buffer, int width) {
        String nonAnsi = buffer.getPlainBuffer();
        return buffer.toString(realColor) + Strings.repeat(" ", Math.max(0, width - nonAnsi.length()));
    }

    public static <T> void drawBar(ANSIBuffer buffer, List<FreqPoint<T>> points, FreqPoint<T> point, int maxBars) {
        long max = calculateMaxFreq(points);
        double avg = calculateAvgFreq(points, point);
        double stdev = calculateStdev(points, point, avg);

        int bars = (int) Math.ceil(maxBars * point.freq() / (double) max);
        int std1 = (int) Math.ceil(maxBars * avg / (double) max);
        int std2 = Math.max(std1 + 1, (int) Math.ceil(maxBars * (avg + stdev) / (double) max));
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
        for (FreqPoint point : points) {
            max = Math.max(max, point.freq());
        }
        return max;
    }

    private static <T> double calculateAvgFreq(List<FreqPoint<T>> points, FreqPoint<T> except) {
        double avg = 0;
        int cnt = 0;
        for (FreqPoint point : points) {
            if (point.freq() == 0 || point == except)
                continue;
            avg += point.freq();
            cnt++;
        }
        return avg / cnt;
    }


    private static <T> double calculateStdev(List<FreqPoint<T>> points, FreqPoint<T> except, double avg) {
        double stdev = 0;
        int cnt = 0;
        for (FreqPoint point : points) {
            if (point.freq() == 0 || point == except)
                continue;
            stdev += Math.pow(point.freq() - avg, 2);
            cnt++;

        }
        return Math.sqrt(stdev / cnt);
    }
}
