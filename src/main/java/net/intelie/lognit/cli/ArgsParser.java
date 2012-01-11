package net.intelie.lognit.cli;

import com.google.inject.Inject;
import net.intelie.lognit.cli.commands.StateKeeper;
import net.intelie.lognit.cli.http.HttpWrapper;
import net.intelie.lognit.cli.model.Welcome;

public class ArgsParser {
    private final StateKeeper state;
    private final HttpWrapper wrapper;

    @Inject
    public ArgsParser(StateKeeper state, HttpWrapper wrapper) {
        this.state = state;
        this.wrapper = wrapper;
    }

    public void run(String[] args) throws Exception {
        state.begin();

        try {
            wrapper.authenticate(args[0], args[1]);
            Welcome welcome = wrapper.request("http://localhost:9006/rest/users/welcome", Welcome.class);
            System.out.println(welcome.getMessage());
        } catch (Exception ex) {
            System.out.println(String.format("%s: %s",
                    ex.getClass().getSimpleName(), ex.getMessage()));
        } finally {
            state.end();
        }
    }
}
