package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.model.Message;

public class MessagePrinter {
    private final UserConsole console;

    @Inject
    public MessagePrinter(UserConsole console) {
        this.console = console;
    }

    public void printMessage(Message message) {
        console.printOut(message.getMessage());
    }
}
