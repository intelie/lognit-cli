package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.model.Message;

public class DefaultMessagePrinter implements MessagePrinter {
    private final UserConsole console;

    public DefaultMessagePrinter(UserConsole console) {
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
