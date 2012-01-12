package net.intelie.lognit.cli.commands;

import com.google.inject.Inject;
import net.intelie.lognit.cli.UserInput;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.model.Welcome;
import net.intelie.lognit.cli.state.UrlComposer;

public class Info implements Command {
    private final UrlComposer url;
    private final UserInput console;
    private final RestClient http;

    @Inject
    public Info(UrlComposer url, UserInput console, RestClient http) {
        this.url = url;
        this.console = console;
        this.http = http;
    }

    @Override
    public String name() {
        return "info";
    }

    @Override
    public void execute(String... args) throws Exception {
        Welcome welcome = http.request(url.deriveFullUrl("/rest/users/welcome"), Welcome.class);
        console.printf("%s\n", welcome);
    }
}
