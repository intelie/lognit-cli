package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.http.RestListenerHandle;
import net.intelie.lognit.cli.http.UnauthorizedException;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Stats;
import net.intelie.lognit.cli.model.StatsSummary;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Collection;

public class RequestRunner {
    public static final String HAS_MISSING_NODES = "(%s): %d node(s) did not respond";
    public static final String NO_MISSING_NODES = "(%s): all nodes responded";
    public static final String NODE_INFO = "node '%s': %d queries / %d docs";
    public static final String TOTAL_INFO = "total: %d queries / %d docs";
    private final UserConsole console;
    private final Lognit lognit;
    private final BufferListenerFactory factory;

    public RequestRunner(UserConsole console, Lognit lognit, BufferListenerFactory factory) {
        this.console = console;
        this.lognit = lognit;
        this.factory = factory;
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
        if (args.length == 0) args = new String[] { "" };
        
        Collection<String> terms = args.length == 1 ?
                lognit.terms("", args[0]).getTerms() :
                lognit.terms(args[0], args[1]).getTerms();

        for (String term : terms)
            console.printOut(term);
    }

    private void executeInfo() throws IOException {
        StatsSummary summary = lognit.stats();

        if (summary.getMissing() > 0)
            console.printOut(HAS_MISSING_NODES, lognit.getServer(), summary.getMissing());
        else
            console.printOut(NO_MISSING_NODES, lognit.getServer());

        console.printOut(TOTAL_INFO, summary.getQueries().size(),  summary.getTotalDocs());
        for (Stats stats : summary.getPerNodes()) {
            console.printOut(NODE_INFO, stats.getNode(), stats.getQueries().size(), stats.getTotalDocs());
        }
    }

    private void executeRequest(UserOptions options) throws IOException {
        BufferListener listener = factory.create(options.isNoColor(), options.isVerbose());
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

