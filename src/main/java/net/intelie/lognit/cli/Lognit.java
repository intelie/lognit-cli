package net.intelie.lognit.cli;

import com.google.inject.Inject;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.model.Welcome;

import java.io.IOException;

public class Lognit {
    public static final String URL_WELCOME = "/rest/users/welcome";
    private final RestClient client;

    @Inject
    public Lognit(RestClient client) {
        this.client = client;
    }

    public Welcome login(String server, String login, String password) throws IOException {
        client.authenticate(server, login, password);
        return info();
    }

    public Welcome info() throws IOException {
        return client.request(URL_WELCOME, Welcome.class);
    }
}
