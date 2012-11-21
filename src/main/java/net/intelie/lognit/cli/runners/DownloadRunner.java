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
import java.util.concurrent.atomic.AtomicLong;

public class DownloadRunner implements Runner {
    public static final String DOWNLOAD_STATUS = "Downloaded %d/%d messages. %d/s. ETA: %.0fs";
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

        final AtomicLong current = new AtomicLong(0), total = new AtomicLong(0);

        TimerTask task = makeTask(current, total);
        timer.schedule(task, 0, 1000);

        try {
            while (stream.hasNext()) {
                DownloadBag bag = stream.next();
                for (Message message : bag.getItems())
                    formatter.print(message, false);
                current.set(bag.getCurrentHit());
                total.set(bag.getTotalHits());
            }
        } finally {
            stream.close();
        }
        task.cancel();
        task.run();

        return 0;
    }

    private TimerTask makeTask(final AtomicLong current, final AtomicLong total) {
        return new TimerTask() {
            long lastHit = 0;

            @Override
            public void run() {
                long currentHit = current.get();
                long totalHits = total.get();
                console.printStill(DOWNLOAD_STATUS,
                        currentHit,
                        totalHits,
                        currentHit - lastHit,
                        calculateETA(currentHit, totalHits));
                lastHit = currentHit;
            }

            private double calculateETA(long currentHit, long totalHits) {
                if (currentHit - lastHit <= 0)
                    return 0;
                return (totalHits - currentHit) / (double) (currentHit - lastHit);
            }
        };
    }
}
