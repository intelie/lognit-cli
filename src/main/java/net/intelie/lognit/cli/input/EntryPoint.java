package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.state.StateKeeper;

public class EntryPoint {

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

    public int run(String... args) {
        state.begin();

        try {
            UserOptions options = new UserOptions(args);
            if (options.isUsage())
                return usage.run();
            else
                return request.run(options);
        } catch (Exception ex) {
            //put some verbose logging here
            console.println("%s: %s", ex.getClass().getSimpleName(), ex.getMessage());
            console.println("run the command with --help for usage help");
            return 3;
        } finally {
            state.end();
        }
    }

}
