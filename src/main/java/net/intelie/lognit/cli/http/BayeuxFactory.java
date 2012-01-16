package net.intelie.lognit.cli.http;

import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;

import java.util.HashMap;

public class BayeuxFactory {
    public BayeuxClient create(String uri) {
        return new BayeuxClient(uri, LongPollingTransport.create(new HashMap<String, Object>()));
    }
}
