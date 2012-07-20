package net.intelie.lognit.cli.formatters;

import jline.ANSIBuffer;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.Message;

import java.util.LinkedHashMap;
import java.util.Map;

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
    public void printMessage(Message message) {
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
    public void printAggregated(Aggregated aggregated) {
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
}
