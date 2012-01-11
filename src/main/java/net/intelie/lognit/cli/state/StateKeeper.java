package net.intelie.lognit.cli.state;

import com.google.inject.Inject;
import org.apache.commons.httpclient.HttpClient;

public class StateKeeper {
    private final HttpClient client;
    private final CookieStorage storage;

    @Inject
    public StateKeeper(HttpClient client, CookieStorage storage) {
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
