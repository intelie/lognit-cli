package net.intelie.lognit.cli.formatters.iem;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.formatters.Formatter;
import net.intelie.lognit.cli.json.Jsonizer;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.AggregatedItem;
import net.intelie.lognit.cli.model.Message;
import net.ser1.stomp.Client;

import java.util.HashMap;
import java.util.Map;

public class IEMSender implements Formatter {
    private static final String QUEUE_NAME = "/queue/events";
    private final UserConsole console;
    private final Client client;
    private final Jsonizer jsonizer;
    private final Map header;

    public IEMSender(UserConsole console, Client client, Jsonizer jsonizer, String eventType) throws Exception {
        this.console = console;
        this.client = client;
        this.jsonizer = jsonizer;
        this.header = makeHeader(eventType);
    }

    private Map makeHeader(String eventType) {
        Map header = new HashMap();
        header.put("eventType", eventType);
        header.put("destination", QUEUE_NAME);
        return header;
    }

    @Override
    public void printStatus(String format, Object... args) {
        console.println(format, args);
    }

    @Override
    public void printMessage(Message message) {
        client.send(QUEUE_NAME, jsonizer.toFlat(message), header);
    }

    @Override
    public void printAggregated(Aggregated aggregated) {
        for (AggregatedItem item : aggregated)
            client.send(QUEUE_NAME, jsonizer.to(item), header);
    }
}
