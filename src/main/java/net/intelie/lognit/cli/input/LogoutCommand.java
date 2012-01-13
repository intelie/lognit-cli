package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.Lognit;

public class LogoutCommand implements Command {
    private final UserInput console;
    private final Lognit lognit;

    @Inject
    public LogoutCommand(UserInput console, Lognit lognit) {
        this.console = console;
        this.lognit = lognit;
    }

    @Override
    public String name() {
        return "logout";
    }

    @Override
    public void execute(ArgsParser parser) throws Exception {
        lognit.logout();
        console.println("Have a nice day!");
    }
}

