package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.http.RestListenerHandle;
import net.intelie.lognit.cli.http.UnauthorizedException;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.StatsSummary;
import net.intelie.lognit.cli.state.Clock;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Collection;

public class RequestRunner {
    public static final String HANDSHAKE = "INFO: handshake (%dms)";
    private final UserConsole console;
    private final Lognit lognit;
    private final InfoRunner infoRunner;
    private final BufferListenerFactory factory;
    private final Clock clock;

    public RequestRunner(UserConsole console, Lognit lognit, InfoRunner infoRunner, BufferListenerFactory factory, Clock clock) {
        this.console = console;
        this.lognit = lognit;
        this.infoRunner = infoRunner;
        this.factory = factory;
        this.clock = clock;
    }

    public int run(UserOptions options) throws IOException {
        prepare(options);

        int retries = options.askPassword() ? 4 : 1;
        while (retries-- > 0) {
            try {
                execute(options);
                return 0;
            } catch (UnauthorizedException ex) {
                console.println("(%s): %s", lognit.getServer(), ex.getMessage());
                if (retries > 0)
                    askPassword(options.getUser());
            }
        }
        return 2;
    }

    private void execute(UserOptions options) throws IOException {
        if (options.isUsage()) return;

        if (options.isComplete()) {
            executeCompletion(options);
        } else if (options.isInfo()) {
            executeInfo();
        } else if (!options.hasQuery()) {
            executeWelcome();
        } else {
            executeRequest(options);
        }
    }

    private void executeWelcome() throws IOException {
        console.println("(%s): %s", lognit.getServer(), lognit.welcome().getMessage());
    }

    private void executeCompletion(UserOptions options) throws IOException {
        String[] args = StringUtils.splitPreserveAllTokens(options.getQuery(), ":", 2);
        if (args.length == 0) args = new String[]{""};

        Collection<String> terms = args.length == 1 ?
                lognit.terms("", args[0]).getTerms() :
                lognit.terms(args[0], args[1]).getTerms();

        for (String term : terms)
            console.printOut(term);
    }

    private void executeInfo() throws IOException {
        infoRunner.printInfo(lognit.getServer(), lognit.stats());
    }

    private void executeRequest(UserOptions options) throws IOException {
        BufferListener listener = factory.create(options.isNoColor(), options.isVerbose());

        RestListenerHandle handle = handshake(options, listener);

        listener.waitHistoric(options.getTimeoutInMilliseconds(), options.getLines());
        if (options.isFollow()) {
            listener.releaseAll();
            console.waitChar('q');
        }
        handle.close();
    }

    private RestListenerHandle handshake(UserOptions options, BufferListener listener) throws IOException {
        long start = clock.currentMillis();
        RestListenerHandle handle = lognit.search(options.getQuery(), options.getLines(), listener);
        if (options.isVerbose())
            console.println(HANDSHAKE, clock.currentMillis() - start);
        return handle;
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

