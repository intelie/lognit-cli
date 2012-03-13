package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Purge;
import net.intelie.lognit.cli.model.PurgeInfo;
import net.intelie.lognit.cli.state.Clock;

import java.io.IOException;

public class PurgeRunner implements Runner {
    public static final String PURGE_DESC = "Purging files that match '%s'...";
    public static final String PURGE_ID = "Task %s";
    public static final int INTERVAL = 1000;
    public static final String STATUS = "%s: %d/%d (%.0f%%). %d errors.";

    private final UserConsole console;
    private final Lognit lognit;
    private final Clock clock;
    private final Runtime runtime;

    public PurgeRunner(UserConsole console, Lognit lognit, Clock clock, Runtime runtime) {
        this.console = console;
        this.lognit = lognit;
        this.clock = clock;
        this.runtime = runtime;
    }

    @Override
    public int run(UserOptions options) throws Exception {
        final Purge purge = lognit.purge(options.getQuery(), options.getLines());

        console.println(PURGE_DESC, options.getQuery());
        console.println(PURGE_ID, purge.getId());

        runtime.addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    lognit.cancelPurge(purge.getId());
                } catch (Exception e) {
                }
            }
        });

        do {
            clock.sleep(INTERVAL);
        } while (printStatus(purge.getId()));
        console.println("");

        return 0;
    }

    private boolean printStatus(String id) throws IOException {
        PurgeInfo info = lognit.purgeInfo(id);
        console.printStill(STATUS,
                info.getStatus(),
                info.getPurged(),
                info.getExpected(),
                getPercentage(info),
                info.getFailed());
        return !info.getStatus().isFinished();
    }

    private double getPercentage(PurgeInfo info) {
        return info.getExpected() > 0 ?
                100.0 * info.getPurged() / info.getExpected() :
                100.0;
    }
}
