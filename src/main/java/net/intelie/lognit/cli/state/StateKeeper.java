package net.intelie.lognit.cli.state;

import com.google.inject.Inject;
import net.intelie.lognit.cli.http.RestClient;
import org.apache.commons.httpclient.HttpClient;

public class StateKeeper {
    private final RestClient client;
    private final RestStateStorage storage;

    @Inject
    public StateKeeper(RestClient client, RestStateStorage storage) {
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
