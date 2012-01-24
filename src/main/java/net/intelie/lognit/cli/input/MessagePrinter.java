package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.model.Message;

public interface MessagePrinter {
    void printStatus(String format, Object... args);

    void printMessage(Message message);
}
