package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.UserConsole;

public class PlainFormatter extends ColoredFormatter {
    public PlainFormatter(UserConsole console) {
        super(console, false);
    }

    public PlainFormatter(UserConsole console, BarsFormatter bars) {
        super(console, bars, false);
    }
}
