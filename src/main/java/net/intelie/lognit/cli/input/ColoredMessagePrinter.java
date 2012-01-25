package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import jline.ANSIBuffer;
import net.intelie.lognit.cli.model.Message;

public class ColoredMessagePrinter implements MessagePrinter {
    public static int NONE = 0;
    public static int RED = 31;
    public static int GREEN = 32;
    public static int YELLOW = 33;
    public static int BLUE = 34;
    public static int MAGENTA = 35;
    public static int CYAN = 36;
    public static int WHITE = 37;

    private final UserConsole console;

    @Inject
    public ColoredMessagePrinter(UserConsole console) {
        this.console = console;
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
        buffer.green(message.getDate() + message.getTime());
        buffer.append(" ");
        buffer.append(message.getFacility());
        buffer.append(" ");
        buffer.append(message.getSeverity());
        buffer.append(" ");
        buffer.yellow(message.getApp());
        buffer.append(" ");
        buffer.append(message.getMessage());

        console.printOut(buffer.toString());
    }
}
