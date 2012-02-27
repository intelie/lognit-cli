package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.input.UserConsole;

public class FormatterSelector {
    private final UserConsole console;
    private final ColoredFormatter colored;
    private final PlainFormatter plain;
    private final JsonFormatter json;

    public FormatterSelector(UserConsole console, ColoredFormatter colored, PlainFormatter plain, JsonFormatter json) {
        this.console = console;
        this.colored = colored;
        this.plain = plain;
        this.json = json;
    }

    public Formatter select(String formatter) {
        if ("colored".equalsIgnoreCase(formatter))
            return console.isTTY() ? colored : plain;
        else if ("plain".equalsIgnoreCase(formatter))
            return plain;
        else if ("json".equalsIgnoreCase(formatter))
            return json;

        throw new IllegalArgumentException("formatter");
    }
}
