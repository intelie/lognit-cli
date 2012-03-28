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

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadRunner implements Runner {
    private final UserConsole console;
    private final Lognit lognit;
    private final FormatterSelector formatters;
    private final Timer timer;

    public DownloadRunner(UserConsole console, Lognit lognit, FormatterSelector formatters, Timer timer) {
        this.console = console;
        this.lognit = lognit;
        this.formatters = formatters;
        this.timer = timer;
    }

    @Override
    public int run(UserOptions options) throws Exception {
        RestStream<DownloadBag> stream = lognit.download(options.getQuery(), options.getLines());
        Formatter formatter = formatters.select(options.getFormat());

        final AtomicInteger messages = new AtomicInteger(0), files = new AtomicInteger(0);

        TimerTask task = makeTask(messages, files);
        timer.schedule(task, 0, 1000);

        try {
            while (stream.hasNext()) {
                DownloadBag bag = stream.next();
                for (Message message : bag.getItems())
                    formatter.printMessage(message);
                messages.addAndGet(bag.getItems().size());
                files.set(bag.getRemainingDocs());
            }
        } finally {
            stream.close();
        }
        task.cancel();
        task.run();

        return 0;
    }

    private TimerTask makeTask(final AtomicInteger messages, final AtomicInteger files) {
        return new TimerTask() {
            int lastMessages = 0;
            int lastFiles = 0;

            @Override
            public void run() {
                int currentMessages = messages.get();
                int currentFiles = files.get();
                console.printStill("Downloaded %d messages. %d/s. Still %d files to analyze. ETA: %.0fs",
                        currentMessages,
                        currentMessages - lastMessages,
                        currentFiles,
                        calculateETA(currentFiles));
                lastMessages = currentMessages;
                lastFiles = currentFiles;
            }

            private double calculateETA(int currentFiles) {
                if (lastFiles - currentFiles <= 0)
                    return 0;
                return currentFiles / (double) (lastFiles - currentFiles);
            }
        };
    }
}
