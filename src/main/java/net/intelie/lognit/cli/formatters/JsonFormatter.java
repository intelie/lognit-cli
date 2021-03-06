package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.json.Jsonizer;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.SearchStats;

import java.util.LinkedHashMap;

public class JsonFormatter implements Formatter {
    private final Jsonizer json;
    private final UserConsole console;

    public JsonFormatter(UserConsole console, Jsonizer json) {
        this.json = json;
        this.console = console;
    }

    @Override
    public void printStatus(String format, Object... args) {
        console.println(format, args);
    }

    @Override
    public void print(Message message, boolean withMetadata) {
        console.printOut("%s", json.to(message));
    }

    @Override
    public void print(Aggregated aggregated) {
        for (LinkedHashMap<String, Object> map : aggregated) {
            console.printOut("%s", json.to(map));
        }
    }

    @Override
    public void print(SearchStats stats) {
        console.printOut("%s", json.to(stats));
    }

}
