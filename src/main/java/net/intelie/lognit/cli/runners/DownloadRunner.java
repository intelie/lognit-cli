package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.formatters.Formatter;
import net.intelie.lognit.cli.formatters.FormatterSelector;
import net.intelie.lognit.cli.http.RestStream;
import net.intelie.lognit.cli.model.DownloadBag;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.state.Clock;

public class DownloadRunner implements Runner {
    private final UserConsole console;
    private final Lognit lognit;
    private final FormatterSelector formatters;
    private final Clock clock;

    public DownloadRunner(UserConsole console, Lognit lognit, FormatterSelector formatters, Clock clock) {
        this.console = console;
        this.lognit = lognit;
        this.formatters = formatters;
        this.clock = clock;
    }

    @Override
    public int run(UserOptions options) throws Exception {
        RestStream<DownloadBag> stream = lognit.download(options.getQuery(), options.getLines());
        Formatter formatter = formatters.select(options.getFormat());
        int messages = 0;
        try {
            while (stream.hasNext()) {
                DownloadBag bag = stream.next();
                for (Message message : bag.getItems())
                    formatter.printMessage(message);
                console.printStill("%d %d", bag.getItems().size(), bag.getRemainingDocs());
            }
        } finally {
            stream.close();
        }

        return 0;
    }
}
