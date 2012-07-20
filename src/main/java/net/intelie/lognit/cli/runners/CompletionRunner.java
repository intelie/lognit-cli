package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.model.Lognit;

import java.io.IOException;
import java.util.Collection;

public class CompletionRunner implements Runner {
    private final UserConsole console;
    private final Lognit lognit;

    public CompletionRunner(UserConsole console, Lognit lognit) {
        this.console = console;
        this.lognit = lognit;
    }

    @Override
    public int run(UserOptions options) throws IOException {
        String[] args = options.getQuery().split(":", 2);
        if (args.length == 0) args = new String[]{""};

        Collection<String> terms = args.length == 1 ?
                lognit.terms("", args[0]).getTerms() :
                lognit.terms(args[0], args[1]).getTerms();

        for (String term : terms)
            console.printOut(term);
        return 0;
    }
}
