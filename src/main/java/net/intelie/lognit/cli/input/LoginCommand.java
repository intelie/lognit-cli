package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.Lognit;
import net.intelie.lognit.cli.model.Welcome;

public class LoginCommand implements Command {
    private final UserInput console;
    private final Lognit lognit;

    @Inject
    public LoginCommand(UserInput console, Lognit lognit) {
        this.console = console;
        this.lognit = lognit;
    }

    @Override
    public String name() {
        return "login";
    }

    @Override
    public void execute(ArgsParser parser) throws Exception {
        String server = parser.required(String.class, "s");
        String login = parser.optional(String.class, "u", null);
        if (login == null)
            login = console.readLine("login: ");
        String password = console.readPassword("password: ");

        Welcome welcome = lognit.login(server, login, password);
        console.println(welcome.getMessage());
    }
}

