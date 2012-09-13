package net.intelie.lognit.cli.formatters;

import jline.ANSIBuffer;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.FreqPoint;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.SearchStats;

import java.util.*;

public class ColoredFormatter implements Formatter {
    private final UserConsole console;
    private boolean colored;

    public ColoredFormatter(UserConsole console) {
        this(console, true);
    }

    public ColoredFormatter(UserConsole console, boolean colored) {
        this.console = console;
        this.colored = colored;
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

        console.printOut(buffer.toString(reallyColored()));
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
        Queue<String>[] C = new Queue[]{new ArrayDeque<String>(), new ArrayDeque<String>()};

        C[0].addAll(ColumnIterator.reprHours(stats.hours(), reallyColored()));
        C[1].addAll(ColumnIterator.reprLastHour(stats.last(), reallyColored()));
        C[1].add("");

        int next = 1;
        for (Map.Entry<String, List<FreqPoint<String>>> entry : stats.fields().entrySet()) {
            C[next].addAll(ColumnIterator.reprField(entry.getKey(), entry.getValue(), reallyColored()));
            next = (next + 1) % 2;
        }


        while (!C[0].isEmpty() || !C[1].isEmpty()) {
            String cLeft = C[0].poll();
            String cRight = C[1].poll();

            console.printOut("%s%s", cLeft != null ? cLeft : "", cRight != null ? cRight : "");
        }

    }

}
