package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.http.RestListenerHandle;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.state.Clock;

import java.io.IOException;

public class SearchRunner implements Runner {
    public static final String HANDSHAKE = "INFO: handshake (%dms)";

    private final UserConsole console;
    private final Lognit lognit;
    private final BufferListenerFactory factory;
    private final Clock clock;
    private final Runtime runtime;

    public SearchRunner(UserConsole console, Lognit lognit, BufferListenerFactory factory, Clock clock, Runtime runtime) {
        this.console = console;
        this.lognit = lognit;
        this.factory = factory;
        this.clock = clock;
        this.runtime = runtime;
    }

    @Override
    public int run(UserOptions options) throws Exception {
        BufferListener listener = factory.create(options.getFormat(), options.isVerbose());

        RestListenerHandle handle = handshake(options, listener);

        try {
            listener.waitHistoric(options.getTimeoutInMilliseconds(), options.getLines());
            if (options.isFollow()) {
                listener.releaseAll();
                clock.sleep(Integer.MAX_VALUE);
            }
        } finally {
            handle.close();
        }

        return 0;
    }

    private RestListenerHandle handshake(UserOptions options, BufferListener listener) throws IOException {
        long start = clock.currentMillis();
        RestListenerHandle handle = lognit.search(options.getQuery(), options.getLines(), listener);
        registerRuntime(handle);

        if (options.isVerbose())
            console.println(HANDSHAKE, clock.currentMillis() - start);
        return handle;
    }

    private void registerRuntime(final RestListenerHandle handle) {
        runtime.addShutdownHook(new Thread() {
            @Override
            public void run() {
                handle.close();
            }
        });
    }
}
