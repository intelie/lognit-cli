package net.intelie.lognit.cli.commands;

import com.google.inject.Inject;
import net.intelie.lognit.cli.UserInput;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.model.Welcome;

public class Login implements Command {
    private final UserInput console;
    private final RestClient http;

    @Inject
    public Login(UserInput console, RestClient http) {
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

        authenticate();

        Welcome welcome = http.request(String.format("http://%s/rest/users/welcome", server), Welcome.class);
        console.printf("%s\n", welcome);
    }

    private void authenticate() {
        console.printf("login: ");
        String login = console.readLine();
        console.printf("password: ");
        String password = console.readPassword();
        http.authenticate(login, password);
    }
}
