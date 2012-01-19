package net.intelie.lognit.cli.input;

public class UserOptions {
    private final UsagePrinter usage;
    private final String[] args;

    public UserOptions(UsagePrinter usage, String... args) {
        this.usage = usage;
        this.args = args;
    }

    public void run() {
        ArgsParser parser = new ArgsParser(args);
        if (parser.flag("-?", "--help")) {
            usage.run();
        }
    }

}
