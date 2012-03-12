package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.model.Lognit;

public class WelcomeRunner implements Runner{
    private final UserConsole console;
    private final Lognit lognit;

    public WelcomeRunner(UserConsole console, Lognit lognit) {
        this.console = console;
        this.lognit = lognit;
    }

    @Override
    public int run(UserOptions options) throws Exception {
        console.println("(%s): %s", lognit.getServer(), lognit.welcome().getMessage());
        return 0;
    }
}
