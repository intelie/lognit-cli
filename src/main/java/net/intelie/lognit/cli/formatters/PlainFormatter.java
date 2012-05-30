package net.intelie.lognit.cli.formatters;

import jline.ANSIBuffer;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.Message;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlainFormatter implements Formatter {
    private final UserConsole console;

    public PlainFormatter(UserConsole console) {
        this.console = console;
    }

    @Override
    public void printStatus(String format, Object... args) {
        console.println(format, args);
    }

    @Override
    public void printMessage(Message message) {
        console.printOut("%s %s %s %s %s %s", message.getHost(), message.formattedDateTime(),
                message.getFacility(), message.getSeverity(), message.getApp(), message.getMessage());
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
                buffer.append("" + entry.getValue());
            }
            console.printOut(buffer.toString());
        }
    }
}
