package net.intelie.lognit.cli.formatters.iem;

import net.intelie.lognit.cli.formatters.Formatter;
import net.intelie.lognit.cli.json.Jsonizer;
import net.intelie.lognit.cli.model.Message;
import net.ser1.stomp.Client;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class IEMSenderFactoryTest {
    public static final String QUEUE_EVENTS = "/queue/events";

    private StompClientFactory clientFactory;
    private IEMSenderFactory factory;
    private Formatter console;
    private Jsonizer jsonizer;

    @Before
    public void setUp() throws Exception {
        clientFactory = mock(StompClientFactory.class, RETURNS_DEEP_STUBS);
        console = mock(Formatter.class);
        jsonizer = mock(Jsonizer.class);
        factory = new IEMSenderFactory(console, jsonizer, clientFactory);
    }

    @Test
    public void whenCreatingWithoutUserAndPassword() throws Exception {
        Client client = clientFactory.create("test", 123, null, null);

        assertCreatedRight(client, "iem://test:123/Event");
    }

    @Test
    public void whenCreatingWithUser() throws Exception {
        Client client = clientFactory.create("test", 123, "user", null);

        assertCreatedRight(client, "iem://user@test:123/Event");
    }

    @Test
    public void whenCreatingWithUserAndPassword() throws Exception {
        Client client = clientFactory.create("test", 123, "user", "pass:pass");

        assertCreatedRight(client, "iem://user:pass:pass@test:123/Event");
    }

    @Test
    public void whenCreatingWithNoPort() throws Exception {
        Client client = clientFactory.create("test", 61613, null, null);

        assertCreatedRight(client, "iem://test/Event");
    }


    private void assertCreatedRight(Client client, String url) throws Exception {
        Message message = mock(Message.class);
        when(jsonizer.toFlat(message)).thenReturn("AAA");

        IEMSender sender = factory.create(url);
        sender.print(message, false);

        verify(client).send(QUEUE_EVENTS, "AAA", makeHeader("Event"));
    }


    private Map makeHeader(String eventType) {
        Map map = new HashMap();
        map.put("destination", QUEUE_EVENTS);
        map.put("eventType", eventType);
        return map;
    }
}
