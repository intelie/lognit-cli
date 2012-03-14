package net.intelie.lognit.cli;

import net.intelie.lognit.cli.runners.AuthenticatorRunner;
import net.intelie.lognit.cli.runners.UsageRunner;
import net.intelie.lognit.cli.state.StateKeeper;

public class EntryPoint {

    private final UserConsole console;
    private final StateKeeper state;
    private final AuthenticatorRunner request;

    public EntryPoint(UserConsole console, StateKeeper state, AuthenticatorRunner runner) {
        this.console = console;
        this.state = state;
        this.request = runner;
    }

    public int run(String... args) {
        state.begin();

        UserOptions options = null;
        try {
            options = new UserOptions(args);
            return request.run(options);
        } catch (Exception ex) {
            //put some verbose logging here
            console.println("%s: %s", ex.getClass().getSimpleName(), ex.getMessage());
            if (options != null && options.isVerbose())
                ex.printStackTrace();
            console.println("run the command with --help for usage help");
            return 3;
        } finally {
            state.end();
        }
    }

}
