package net.intelie.lognit.cli.http;

import org.cometd.client.BayeuxClient;

import java.util.concurrent.atomic.AtomicBoolean;

public class BayeuxHandle implements RestListenerHandle {
    private final AtomicBoolean closed;
    private final BayeuxClient client;
    private volatile boolean valid = true;

    public BayeuxHandle(BayeuxClient client) {
        this.client = client;
        this.closed = new AtomicBoolean(false);
    }

    @Override
    public void waitDisconnected() {
        while (valid)
            if (this.client.waitFor(1000, BayeuxClient.State.UNCONNECTED, BayeuxClient.State.DISCONNECTED))
                return;
    }

    public void invalidate() {
        this.valid = false;
    }

    @Override
    public void close() {
        if (!closed.getAndSet(true))
            this.client.disconnect();
    }
}
