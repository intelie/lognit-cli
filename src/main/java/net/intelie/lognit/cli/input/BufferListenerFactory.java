package net.intelie.lognit.cli.input;

import com.google.inject.Inject;

public class BufferListenerFactory {
    private final UserConsole console;
    private final ColoredMessagePrinter coloredPrinter;
    private final DefaultMessagePrinter defaultPrinter;

    @Inject
    public BufferListenerFactory(UserConsole console, ColoredMessagePrinter coloredPrinter, DefaultMessagePrinter defaultPrinter) {
        this.console = console;
        this.coloredPrinter = coloredPrinter;
        this.defaultPrinter = defaultPrinter;
    }

    public BufferListener create(boolean forceNoColor) {
        if (!console.isTTY() || forceNoColor)
            return new BufferListener(defaultPrinter);
        return new BufferListener(coloredPrinter);
    }
}
