package net.intelie.lognit.cli.formatters;

import jline.ANSIBuffer;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.Message;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlainFormatter extends ColoredFormatter {
    public PlainFormatter(UserConsole console) {
        super(console, false);
    }

    public PlainFormatter(UserConsole console, BarsFormatter bars) {
        super(console, bars, false);
    }
}
