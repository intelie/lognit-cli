package net.intelie.lognit.cli.http;

import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;

import java.util.HashMap;

public class BayeuxFactory {
    public BayeuxClient create(String uri) {
        return new BayeuxClient(uri, LongPollingTransport.create(null));
    }
}
