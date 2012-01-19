package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.model.Lognit;

public class UserOptions {
    private final UsagePrinter usage;
    private final Lognit lognit;
    private final String[] args;

    public UserOptions(UsagePrinter usage, Lognit lognit, String... args) {
        this.usage = usage;
        this.lognit = lognit;
        this.args = args;
    }

    public void run() {
        ArgsParser parser = new ArgsParser(args);
        parseHelp(parser);
        parseServer(parser);

    }

    private void parseServer(ArgsParser parser) {
        String server = parser.option(String.class, "-s", "--server");
        if (server != null)
            lognit.setServer(server);
    }

    private void parseHelp(ArgsParser parser) {
        if (parser.flag("-?", "--help"))
            usage.run();
    }

}
