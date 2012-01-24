package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.model.Message;

public class MessagePrinter {
    private final UserConsole console;

    @Inject
    public MessagePrinter(UserConsole console) {
        this.console = console;
    }

    public void printStatus(String format, Object... args) {
        console.println(format, args);
    }

    public void printMessage(Message message) {
        console.printOut("%s %s%s %s %s %s %s", message.getHost(), message.getDate(), message.getTime(),
                message.getFacility(), message.getSeverity(), message.getApp(), message.getMessage());
    }
}
