package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.http.UnauthorizedException;
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
        boolean askPassword = user == null || password == null;

        if (server != null)
            lognit.setServer(server);
        if (!askPassword)
            lognit.authenticate(user, password);

        boolean success = false;
        int retries = askPassword ? 4 : 1;
        while (!success && retries-- > 0) {
            try {
                console.println(lognit.welcome().getMessage());
                success = true;
            } catch (UnauthorizedException ex) {
                console.println(ex.getMessage());
                if (retries > 0) {
                    String newUser = user == null ? console.readLine("login: ") : user;
                    String newPass = console.readPassword("%s's password: ", newUser);
                    lognit.authenticate(newUser, newPass);
                }
            }
        }
    }
}
