package net.intelie.lognit.cli.formatters.iem;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.formatters.ColoredFormatter;
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
    public static final String MESSAGES_SENT = "event: %d message(s) sent";
    private final Formatter console;
    private final Client client;
    private final Jsonizer jsonizer;
    private final Map header;

    public IEMSender(Formatter console, Client client, Jsonizer jsonizer, String eventType) {
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
        console.printStatus(format, args);
    }

    @Override
    public void printMessage(Message message) {
        console.printMessage(message);
        console.printStatus(MESSAGES_SENT, 1);
        client.send(QUEUE_NAME, jsonizer.toFlat(message), header);
    }

    @Override
    public void printAggregated(Aggregated aggregated) {
        console.printAggregated(aggregated);
        console.printStatus(MESSAGES_SENT, aggregated.size());
        for (AggregatedItem item : aggregated)
            client.send(QUEUE_NAME, jsonizer.to(item), header);
    }
}
