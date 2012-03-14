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
    public static final String PURGE_ID = "(%s): Task '%s'";
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
        if (options.isCancelPurges())
            return runCancelAll();

        
        final Purge purge = runOption(options);

        console.println(PURGE_ID, lognit.getServer(), purge.getId());

        registerShutdownHook(purge);

        do {
            clock.sleep(INTERVAL);
        } while (printStatus(purge.getId()));
        console.println("");

        return 0;
    }

    private int runCancelAll() throws IOException {
        lognit.cancelAllPurges();
        console.println("All purges cancelled.");
        return 0;
    }

    private Purge runOption(UserOptions options) throws IOException {
        return options.isPurge() ?
                lognit.purge(options.getQuery(), options.getLines()) :
                lognit.unpurge();
    }

    private void registerShutdownHook(final Purge purge) {
        runtime.addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    lognit.cancelPurge(purge.getId());
                } catch (Exception e) {
                }
            }
        });
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
