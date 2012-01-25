package net.intelie.lognit.cli.http;

import org.cometd.client.BayeuxClient;

public class BayeuxHandle implements RestListenerHandle {
    private final BayeuxClient client;

    public BayeuxHandle(BayeuxClient client) {
        this.client = client;
    }

    @Override
    public void close() {
        this.client.disconnect();
    }
}
