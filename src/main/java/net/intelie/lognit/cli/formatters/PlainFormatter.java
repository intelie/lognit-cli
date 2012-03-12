package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.model.Message;

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
}
