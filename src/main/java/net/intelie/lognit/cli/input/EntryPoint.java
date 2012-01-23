package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.state.StateKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryPoint {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserConsole console;
    private final UserOptions options;
    private final StateKeeper state;

    @Inject
    public EntryPoint(UserConsole console, UserOptions options, StateKeeper state) {
        this.console = console;
        this.options = options;
        this.state = state;
    }

    public void run(String... args) {
        state.begin();

        try {
            options.run(args);
        } catch (Exception ex) {
            logger.warn("An error has ocurred. Sad.", ex);
            console.println("%s: %s", ex.getClass().getSimpleName(), ex.getMessage());
        } finally {
            state.end();
        }
    }

}
