package net.intelie.lognit.cli.http;

import org.cometd.client.BayeuxClient;

import java.util.concurrent.atomic.AtomicBoolean;

public class BayeuxHandle implements RestListenerHandle {
    private final AtomicBoolean closed;
    private final BayeuxClient client;

    public BayeuxHandle(BayeuxClient client) {
        this.client = client;
        this.closed = new AtomicBoolean(false);
    }

    @Override
    public void waitDisconnected() {
        this.client.waitFor(Long.MAX_VALUE, BayeuxClient.State.UNCONNECTED, BayeuxClient.State.DISCONNECTED);
    }

    @Override
    public void close() {
        if (!closed.getAndSet(true))
            this.client.disconnect();
    }
}
