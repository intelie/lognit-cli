package net.intelie.lognit.cli.input;

import com.google.inject.Inject;

import java.io.IOException;

public class UserOptions {
    private final UsageRunner usage;
    private final RequestRunner runner;

    @Inject
    public UserOptions(UsageRunner usage, RequestRunner runner) {
        this.usage = usage;
        this.runner = runner;
    }

    public void run(String... args) throws IOException {
        ArgsParser parser = new ArgsParser(args);
        if (parser.flag("-?", "--help")) {
            usage.run();
            return;
        }
        String server = parser.option(String.class, "-s", "--server");
        String username = parser.option(String.class, "-u", "--user");
        String password = parser.option(String.class, "-p", "--pass", "--password");
        String query = parser.text();

        runner.run(server, username, password, query);
    }
}
