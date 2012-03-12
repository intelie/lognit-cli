package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.model.Lognit;

public class PurgeRunner implements Runner{
    private final UserConsole console;
    private final Lognit lognit;

    public PurgeRunner(UserConsole console, Lognit lognit) {
        this.console = console;
        this.lognit = lognit;
    }

    @Override
    public int run(UserOptions options) throws Exception {
        console.println(lognit.purge(options.getQuery(), options.getLines()).getId());
        return 0;
    }
}
