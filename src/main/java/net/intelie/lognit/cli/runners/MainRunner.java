package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserOptions;

public class MainRunner implements Runner {
    private final SearchRunner search;
    private final InfoRunner info;
    private final CompletionRunner completion;
    private final UsageRunner usage;
    private final WelcomeRunner welcome;
    private final PurgeRunner purge;

    public MainRunner(SearchRunner search,
                      InfoRunner info,
                      CompletionRunner completion,
                      UsageRunner usage,
                      WelcomeRunner welcome,
                      PurgeRunner purge) {
        this.search = search;
        this.info = info;
        this.completion = completion;
        this.usage = usage;
        this.welcome = welcome;
        this.purge = purge;
    }

    @Override
    public int run(UserOptions options) throws Exception {
        if (options.isUsage())
            return usage.run(options);
        else if (options.isInfo())
            return info.run(options);
        else if (options.isComplete())
            return completion.run(options);
        else if (options.isPurge()) 
            return purge.run(options);
        else if (options.hasQuery())
            return search.run(options);
        else
            return welcome.run(options);
    }
}
