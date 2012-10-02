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
    public static final String REALTIME_DISCONNECTED = "Realtime results disconnected";

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
        try {
            BufferListener listener = factory.create(options.getFormat(), options.isStats());

            RestListenerHandle handle = handshake(options, listener);

            try {
                listener.waitHistoric(options.getTimeoutInMilliseconds(), options.getLines());
                if (options.isFollow()) {
                    listener.releaseAll();
                    handle.waitDisconnected();
                    throw new RetryConnectionException(options.realtimeOnly(), REALTIME_DISCONNECTED);
                }
                return 0;
            } finally {
                handle.close();
            }
        } catch (Exception e) {
            if (options.isFollow() && !(e instanceof RetryConnectionException))
                throw new RetryConnectionException(options, e);
            else
                throw e;
        }
    }

    private RestListenerHandle handshake(UserOptions options, BufferListener listener) throws IOException {
        long start = clock.currentMillis();
        RestListenerHandle handle = lognit.search(options.getQuery(), options.getLines(), options.isFollow(), options.isStats(), listener);
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
