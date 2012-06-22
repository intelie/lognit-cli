package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.http.UnauthorizedException;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.state.Clock;

public class AuthenticatorRunner implements Runner {
    private final UserConsole console;
    private final Lognit lognit;
    private final Clock clock;
    private final MainRunner runner;

    public AuthenticatorRunner(UserConsole console, Lognit lognit, Clock clock, MainRunner runner) {
        this.console = console;
        this.lognit = lognit;
        this.clock = clock;
        this.runner = runner;
    }

    @Override
    public int run(UserOptions options) throws Exception {
        int retries = options.askPassword() ? 4 : 1;

        while (retries-- > 0) {
            try {
                prepare(options);
                return runner.run(options);
            } catch (RetryConnectionException e) {
                stacktrace(options, e);
                printServerMessage(e.getMessage());
                clock.sleep(2000);
                options = e.options();
                if (e.getCause() instanceof UnauthorizedException && options.askPassword())
                    askPassword(options.getUser());
                retries++;
            } catch (UnauthorizedException e) {
                stacktrace(options, e);
                printServerMessage(e.getMessage());
                if (retries > 0)
                    askPassword(options.getUser());
            }
        }
        return 2;
    }

    private void printServerMessage(String msg) {
        console.println("(%s): %s", lognit.getServer(), msg);
    }

    private void stacktrace(UserOptions options, Exception e) {
        if (options.isVerbose())
            e.printStackTrace();
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
        if (opts.isForceLogin()) {
            printServerMessage("forcing login");
            askPassword(opts.getUser());
        }
    }
}

