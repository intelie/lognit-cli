package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.state.StateKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryPoint {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserConsole console;
    private final StateKeeper state;
    private final RequestRunner request;
    private final UsageRunner usage;

    @Inject
    public EntryPoint(UserConsole console, StateKeeper state, RequestRunner request, UsageRunner usage) {
        this.console = console;
        this.state = state;
        this.request = request;
        this.usage = usage;
    }

    public void run(String... args) {
        state.begin();

        try {
            UserOptions options = new UserOptions(args);
            if (options.isUsage())
                usage.run();
            else
                request.run(options);
        } catch (Exception ex) {
            logger.warn("An error has ocurred. Sad.", ex);
            console.println("%s: %s", ex.getClass().getSimpleName(), ex.getMessage());
        } finally {
            state.end();
        }
    }

}
