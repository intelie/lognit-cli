package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.http.RestListenerHandle;
import net.intelie.lognit.cli.http.UnauthorizedException;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.state.Clock;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Collection;

public class AuthenticatorRunner implements Runner{
    private final UserConsole console;
    private final Lognit lognit;
    private final MainRunner runner;

    public AuthenticatorRunner(UserConsole console, Lognit lognit, MainRunner runner) {
        this.console = console;
        this.lognit = lognit;
        this.runner = runner;
    }

    @Override
    public int run(UserOptions options) throws Exception {
        prepare(options);

        int retries = options.askPassword() ? 4 : 1;
        while (retries-- > 0) {
            try {
                runner.run(options);
                return 0;
            } catch (UnauthorizedException ex) {
                console.println("(%s): %s", lognit.getServer(), ex.getMessage());
                if (retries > 0)
                    askPassword(options.getUser());
            }
        }
        return 2;
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

