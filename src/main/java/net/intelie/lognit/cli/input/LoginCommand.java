package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.Lognit;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.model.Welcome;

import java.net.MalformedURLException;

public class LoginCommand implements Command {
    private final UserInput console;
    private final RestClient http;

    @Inject
    public LoginCommand(UserInput console, RestClient http) {
        this.console = console;
        this.http = http;
    }

    @Override
    public String name() {
        return "login";
    }

    @Override
    public void execute(String... args) throws Exception {
        String server = args[0];

        authenticate(server);

        Welcome welcome = http.request(Lognit.welcome(), Welcome.class);
        console.printf("%s\n", welcome.getMessage());
    }

    private void authenticate(String server) throws MalformedURLException {
        String login = console.readLine("login: ");
        String password = console.readPassword("password: ");
        http.authenticate(server,  login, password);
    }
}
