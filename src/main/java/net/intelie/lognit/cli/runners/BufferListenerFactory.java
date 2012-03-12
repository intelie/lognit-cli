package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.formatters.FormatterSelector;

public class BufferListenerFactory {
    private final FormatterSelector selector;

    public BufferListenerFactory(FormatterSelector selector) {
        this.selector = selector;
    }

    public BufferListener create(String format, boolean verbose) {
        return new BufferListener(selector.select(format), verbose);
    }
}
