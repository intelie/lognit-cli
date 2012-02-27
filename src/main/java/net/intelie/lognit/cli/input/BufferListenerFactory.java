package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.formatters.ColoredFormatter;
import net.intelie.lognit.cli.formatters.FormatterSelector;
import net.intelie.lognit.cli.formatters.PlainFormatter;

public class BufferListenerFactory {
    private final FormatterSelector selector;

    public BufferListenerFactory(FormatterSelector selector) {
        this.selector = selector;
    }

    public BufferListener create(String format, boolean verbose) {
        return new BufferListener(selector.select(format), verbose);
    }
}
