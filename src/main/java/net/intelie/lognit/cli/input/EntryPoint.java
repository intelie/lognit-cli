package net.intelie.lognit.cli.input;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import net.intelie.lognit.cli.state.StateKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class EntryPoint {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserInput console;
    private final UserOptions options;
    private final StateKeeper state;

    @Inject
    public EntryPoint(UserInput console, UserOptions options, StateKeeper state) {
        this.console = console;
        this.options = options;
        this.state = state;
    }

    public void run() {
        state.begin();

        try {
            options.run();
        } catch (Exception ex) {
            logger.warn("An error has ocurred. Sad.", ex);
            console.println("%s: %s", ex.getClass().getSimpleName(), ex.getMessage());
        } finally {
            state.end();
        }
    }

}
