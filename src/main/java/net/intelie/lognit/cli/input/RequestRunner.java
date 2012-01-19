package net.intelie.lognit.cli.input;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import net.intelie.lognit.cli.http.UnauthorizedException;
import net.intelie.lognit.cli.model.Lognit;

import java.io.IOException;

public class RequestRunner {
    private final UserInput console;
    private final Lognit lognit;
    private final BufferListener listener;

    @Inject
    public RequestRunner(UserInput console, Lognit lognit, BufferListener listener) {
        this.console = console;
        this.lognit = lognit;
        this.listener = listener;
    }

    public void run(String server, String user, String password, String query) throws IOException {
        boolean askPassword = prepare(server, user, password);

        int retries = askPassword ? 4 : 1;
        while (retries-- > 0) {
            try {
                execute(query);
                break;
            } catch (UnauthorizedException ex) {
                console.println(ex.getMessage());
                if (retries > 0)
                    askPassword(user);
            }
        }
    }

    private void execute(String query) throws IOException {
        if (Strings.isNullOrEmpty(query))
            console.println(lognit.welcome().getMessage());
        else
            lognit.search(query, listener);
    }

    private void askPassword(String user) {
        String newUser = user == null ? console.readLine("login: ") : user;
        String newPass = console.readPassword("%s's password: ", newUser);
        lognit.authenticate(newUser, newPass);
    }

    private boolean prepare(String server, String user, String password) {
        boolean askPassword = user == null || password == null;
        if (server != null)
            lognit.setServer(server);
        if (!askPassword)
            lognit.authenticate(user, password);
        return askPassword;
    }
}
