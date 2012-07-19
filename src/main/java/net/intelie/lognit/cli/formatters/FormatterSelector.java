package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.formatters.iem.IEMSenderFactory;

public class FormatterSelector {
    private final UserConsole console;
    private final ColoredFormatter colored;
    private final PlainFormatter plain;
    private final JsonFormatter json;
    private final FlatJsonFormatter flatJson;
    private final IEMSenderFactory iem;

    public FormatterSelector(UserConsole console,
                             ColoredFormatter colored,
                             PlainFormatter plain,
                             JsonFormatter json,
                             FlatJsonFormatter flatJson,
                             IEMSenderFactory iem) {
        this.console = console;
        this.colored = colored;
        this.plain = plain;
        this.json = json;
        this.flatJson = flatJson;
        this.iem = iem;
    }

    public Formatter select(String formatter) throws Exception {
        if (formatter == null)
            throw new IllegalArgumentException("formatter");

        if ("colored".equalsIgnoreCase(formatter))
            return console.isTTY() ? colored : plain;
        else if ("plain".equalsIgnoreCase(formatter))
            return plain;
        else if ("json".equalsIgnoreCase(formatter))
            return json;
        else if ("flat-json".equalsIgnoreCase(formatter))
            return flatJson;
        else if(formatter.startsWith("iem"))
            return iem.create(formatter);

        throw new IllegalArgumentException("formatter");
    }
}
