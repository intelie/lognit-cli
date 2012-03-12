package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.UserConsole;

public class FormatterSelector {
    private final UserConsole console;
    private final ColoredFormatter colored;
    private final PlainFormatter plain;
    private final JsonFormatter json;
    private final FlatJsonFormatter flatJson;

    public FormatterSelector(UserConsole console, ColoredFormatter colored, PlainFormatter plain, JsonFormatter json, FlatJsonFormatter flatJson) {
        this.console = console;
        this.colored = colored;
        this.plain = plain;
        this.json = json;
        this.flatJson = flatJson;
    }

    public Formatter select(String formatter) {
        if ("colored".equalsIgnoreCase(formatter))
            return console.isTTY() ? colored : plain;
        else if ("plain".equalsIgnoreCase(formatter))
            return plain;
        else if ("json".equalsIgnoreCase(formatter))
            return json;
        else if ("flat-json".equalsIgnoreCase(formatter))
            return flatJson;

        throw new IllegalArgumentException("formatter");
    }
}
