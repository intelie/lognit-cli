package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.formatters.ColoredFormatter;
import net.intelie.lognit.cli.formatters.PlainFormatter;

public class BufferListenerFactory {
    private final UserConsole console;
    private final ColoredFormatter coloredPrinter;
    private final PlainFormatter defaultPrinter;

    @Inject
    public BufferListenerFactory(UserConsole console, ColoredFormatter coloredPrinter, PlainFormatter defaultPrinter) {
        this.console = console;
        this.coloredPrinter = coloredPrinter;
        this.defaultPrinter = defaultPrinter;
    }

    public BufferListener create(boolean forceNoColor, boolean verbose) {
        if (!console.isTTY() || forceNoColor)
            return new BufferListener(defaultPrinter, verbose);
        return new BufferListener(coloredPrinter, verbose);
    }
}
