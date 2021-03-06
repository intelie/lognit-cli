package net.intelie.lognit.cli.state;

import net.intelie.lognit.cli.http.RestClient;

public class StateKeeper {
    private final RestClient client;
    private final RestStateStorage storage;

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

    public void register(Runtime runtime) {
        runtime.addShutdownHook(new Thread() {
            @Override
            public void run() {
                StateKeeper.this.end();
            }
        });
    }
}
