package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.json.Jsonizer;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.SearchStats;

import java.util.LinkedHashMap;

public class FlatJsonFormatter implements Formatter {
    private final Jsonizer json;
    private final UserConsole console;

    public FlatJsonFormatter(UserConsole console, Jsonizer json) {
        this.json = json;
        this.console = console;
    }

    @Override
    public void printStatus(String format, Object... args) {
        console.println(format, args);
    }

    @Override
    public void print(Message message) {
        console.printOut("%s", json.toFlat(message));
    }

    @Override
    public void print(Aggregated aggregated) {
        for (LinkedHashMap<String, Object> map : aggregated) {
            console.printOut("%s", json.toFlat(map));
        }
    }

    @Override
    public void print(SearchStats stats) {
        console.printOut("%s", json.to(stats));
    }
}
