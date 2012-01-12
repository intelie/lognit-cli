package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.Lognit;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.model.Welcome;

public class InfoCommand implements Command {
    private final UserInput console;
    private final RestClient http;

    @Inject
    public InfoCommand(UserInput console, RestClient http) {
        this.console = console;
        this.http = http;
    }

    @Override
    public String name() {
        return "info";
    }

    @Override
    public void execute(String... args) throws Exception {
        Welcome welcome = http.request(Lognit.welcome(), Welcome.class);
        console.printf("%s\n", welcome.getMessage());
    }
}
