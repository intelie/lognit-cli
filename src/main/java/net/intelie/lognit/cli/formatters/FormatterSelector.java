package net.intelie.lognit.cli.formatters;

import com.google.inject.Inject;
import net.intelie.lognit.cli.input.UserConsole;

public class FormatterSelector {
    private final UserConsole console;
    private final ColoredFormatter colored;
    private final PlainFormatter plain;

    @Inject
    public FormatterSelector(UserConsole console, ColoredFormatter colored, PlainFormatter plain) {
        this.console = console;
        this.colored = colored;
        this.plain = plain;
    }

    public Formatter select(String formatter) {
        if ("colored".equalsIgnoreCase(formatter))
            return console.isTTY() ? colored : plain;
        else if ("plain".equalsIgnoreCase(formatter))
            return plain;

        throw new IllegalArgumentException("formatter");
    }
}
