package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Pause;

public class PauseRunner implements Runner {
    public static final String PAUSED = "Indexing in %d lognit instances were paused.";
    public static final String RESUMED = "Indexing in %d lognit instances were resumed.";
    private final UserConsole console;
    private final Lognit lognit;

    public PauseRunner(UserConsole console, Lognit lognit) {
        this.console = console;
        this.lognit = lognit;
    }

    @Override
    public int run(UserOptions options) throws Exception {
        if (options.isPause()) {
            Pause pause = lognit.pause(options.isAll());
            console.println(PAUSED, pause.getCount());
        } else {
            Pause resume = lognit.resume(options.isAll());
            console.println(RESUMED, resume.getCount());
        }

        return 0;
    }
}
