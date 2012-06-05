package net.intelie.lognit.cli.http;

import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;

import java.util.HashMap;

public class BayeuxFactory {
    public BayeuxClient create(String uri) {
        LongPollingTransport transport = LongPollingTransport.create(new HashMap<String, Object>());
        return new BayeuxClient(uri, transport);
    }
}
