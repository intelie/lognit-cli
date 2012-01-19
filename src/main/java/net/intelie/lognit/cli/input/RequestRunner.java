package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.model.Lognit;

import java.io.IOException;

public class RequestRunner {
    private final UserInput console;
    private final Lognit lognit;

    @Inject
    public RequestRunner(UserInput console, Lognit lognit) {
        this.console = console;
        this.lognit = lognit;
    }

    public void run(String server, String user, String password, String query) throws IOException {
        if (server != null)
            lognit.setServer(server);
        console.println(lognit.welcome().getMessage());
    }
}
