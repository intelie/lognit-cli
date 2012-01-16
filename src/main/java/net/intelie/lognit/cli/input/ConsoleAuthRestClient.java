package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestListener;
import net.intelie.lognit.cli.http.RestState;

import java.io.IOException;

public class ConsoleAuthRestClient implements RestClient {
    private final RestClient client;

    public ConsoleAuthRestClient(RestClient client) {
        this.client = client;
    }

    @Override
    public RestState getState() {
        return client.getState();
    }

    @Override
    public void setState(RestState state) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setServer(String server) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void authenticate(String username, String password) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T request(String uri, Class<T> type) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> void listen(String uri, Class<T> type, RestListener<T> listener) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
