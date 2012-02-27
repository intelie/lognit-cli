package net.intelie.lognit.cli.formatters;

import jline.ANSIBuffer;
import net.intelie.lognit.cli.input.UserConsole;
import net.intelie.lognit.cli.model.Message;

public class ColoredFormatter implements Formatter {
    private final UserConsole console;

    public ColoredFormatter(UserConsole console) {
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
        buffer.green(message.formattedDateTime());
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
