package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.http.RestListenerHandle;
import net.intelie.lognit.cli.http.UnauthorizedException;
import net.intelie.lognit.cli.model.Lognit;

import java.io.IOException;

public class RequestRunner {
    private final UserConsole console;
    private final Lognit lognit;
    private final BufferListenerFactory factory;

    @Inject
    public RequestRunner(UserConsole console, Lognit lognit, BufferListenerFactory factory) {
        this.console = console;
        this.lognit = lognit;
        this.factory = factory;
    }

    public void run(UserOptions options) throws IOException {
        prepare(options);

        int retries = options.askPassword() ? 4 : 1;
        while (retries-- > 0) {
            try {
                execute(options);
                break;
            } catch (UnauthorizedException ex) {
                console.println("(%s): %s", lognit.getServer(), ex.getMessage());
                if (retries > 0)
                    askPassword(options.getUser());
            }
        }
    }

    private void execute(UserOptions options) throws IOException {
        if (options.isUsage()) return;

        if (options.isInfo()) {
            console.println("(%s): %s", lognit.getServer(), lognit.welcome().getMessage());
        } else {
            executeRequest(options);
        }
    }

    private void executeRequest(UserOptions options) throws IOException {
        BufferListener listener = factory.create(options.isNoColor());
        RestListenerHandle handle = lognit.search(options.getQuery(), options.getLines(), listener);
        listener.waitHistoric(options.getTimeoutInMilliseconds(), options.getLines());
        if (options.isFollow()) {
            listener.releaseAll();
            console.waitChar('q');
        }
        handle.close();
    }

    private void askPassword(String user) {
        String newUser = user == null ? console.readLine("login: ") : user;
        String newPass = console.readPassword("%s's password: ", newUser);
        lognit.authenticate(newUser, newPass);
    }

    private void prepare(UserOptions opts) {
        if (opts.hasServer())
            lognit.setServer(opts.getServer());
        if (!opts.askPassword())
            lognit.authenticate(opts.getUser(), opts.getPassword());
    }
}
