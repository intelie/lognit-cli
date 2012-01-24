package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.http.RestListenerHandle;
import net.intelie.lognit.cli.http.UnauthorizedException;
import net.intelie.lognit.cli.model.Lognit;

import java.io.IOException;

public class RequestRunner {
    private final UserConsole console;
    private final Lognit lognit;
    private final BufferListener listener;

    @Inject
    public RequestRunner(UserConsole console, Lognit lognit, BufferListener listener) {
        this.console = console;
        this.lognit = lognit;
        this.listener = listener;
    }

    public void run(RequestOptions options) throws IOException {
        boolean askPassword = prepare(options.getServer(), options.getUser(), options.getPassword());

        int retries = askPassword ? 4 : 1;
        while (retries-- > 0) {
            try {
                execute(options);
                break;
            } catch (UnauthorizedException ex) {
                console.println(ex.getMessage());
                if (retries > 0)
                    askPassword(options.getUser());
            }
        }
    }

    private void execute(RequestOptions options) throws IOException {
        if (!options.hasQuery()) {
            console.println(lognit.welcome().getMessage());
        } else {
            RestListenerHandle handle = lognit.search(options.getQuery(), options.getLines(), listener);
            listener.waitHistoric(options.getTimeoutInMilliseconds(), options.getLines());
            if (options.isFollow()) {
                listener.releaseAll();
                console.waitChar('q');
            }
            handle.close();
        }
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
