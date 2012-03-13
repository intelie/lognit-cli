package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Purge;

public class PurgeRunner implements Runner {
    private final UserConsole console;
    private final Lognit lognit;

    public PurgeRunner(UserConsole console, Lognit lognit) {
        this.console = console;
        this.lognit = lognit;
    }

    @Override
    public int run(UserOptions options) throws Exception {
        Purge purge = lognit.purge(options.getQuery(), options.getLines());
        console.println(purge.getId());
        for (int i = 0; i < 1000; i++) {
            console.printStill("%d", i);
            Thread.sleep(1000);
        }
        return 0;
    }
}
