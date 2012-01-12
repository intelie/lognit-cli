package net.intelie.lognit.cli.state;

import com.google.inject.Inject;
import org.apache.commons.httpclient.HttpClient;

public class StateKeeper {
    private final HttpClient client;
    private final HttpClientStorage storage;

    @Inject
    public StateKeeper(HttpClient client, HttpClientStorage storage) {
        this.client = client;
        this.storage = storage;
    }

    public void begin() {
        storage.recoverTo(client);
    }

    public void end() {
        storage.storeFrom(client);
    }
}
