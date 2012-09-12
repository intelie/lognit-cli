package net.intelie.lognit.cli.formatters.iem;

import net.intelie.lognit.cli.formatters.Formatter;
import net.intelie.lognit.cli.json.Jsonizer;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.AggregatedItem;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.SearchStats;
import net.ser1.stomp.Client;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class IEMSenderTest {

    public static final String QUEUE_EVENTS = "/queue/events";
    private Formatter console;
    private Client client;
    private Jsonizer jsonizer;

    @Before
    public void setUp() throws Exception {
        console = mock(Formatter.class);
        client = mock(Client.class);
        jsonizer = mock(Jsonizer.class);
    }

    @Test
    public void whenSendingMessageEvent() throws Exception {
        IEMSender sender = new IEMSender(console, client, jsonizer, "test");

        Message message = mock(Message.class);
        when(jsonizer.toFlat(message)).thenReturn("AAA");

        sender.print(message);
        verify(console).print(message);
        verify(client).send(QUEUE_EVENTS, "AAA", makeHeader("test"));
    }

    @Test
    public void whenSendingStatsEvent() throws Exception {
        IEMSender sender = new IEMSender(console, client, jsonizer, "test");

        SearchStats stats = mock(SearchStats.class);
        when(jsonizer.to(stats)).thenReturn("AAA");

        sender.print(stats);
        verify(console).print(stats);
        verify(client).send(QUEUE_EVENTS, "AAA", makeHeader("test"));
    }

    @Test
    public void whenSendingAggregationEvent() throws Exception {
        IEMSender sender = new IEMSender(console, client, jsonizer, "test");

        AggregatedItem item1 = mock(AggregatedItem.class);
        AggregatedItem item2 = mock(AggregatedItem.class);

        Aggregated aggregated = new Aggregated(item1, item2);

        when(jsonizer.to(item1)).thenReturn("AAA");
        when(jsonizer.to(item2)).thenReturn("BBB");

        sender.print(aggregated);
        verify(console).print(aggregated);

        verify(client).send(QUEUE_EVENTS, "AAA", makeHeader("test"));
        verify(client).send(QUEUE_EVENTS, "BBB", makeHeader("test"));
    }

    @Test
    public void printingStatusPrintToConsole() throws Exception {
        IEMSender sender = new IEMSender(console, client, jsonizer, "test");
        sender.printStatus("abc", 1, 2);
        verify(console).printStatus("abc", 1, 2);
    }

    private Map makeHeader(String eventType) {
        Map map = new HashMap();
        map.put("destination", QUEUE_EVENTS);
        map.put("eventType", eventType);
        return map;
    }
}
