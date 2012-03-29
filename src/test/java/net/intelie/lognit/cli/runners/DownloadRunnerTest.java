package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.BlockingIterator;
import net.intelie.lognit.cli.SupportTimer;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.formatters.Formatter;
import net.intelie.lognit.cli.formatters.FormatterSelector;
import net.intelie.lognit.cli.http.RestStream;
import net.intelie.lognit.cli.model.DownloadBag;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Message;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.fail;
import static org.mockito.Mockito.*;

public class DownloadRunnerTest {

    private SupportTimer timer;
    private FormatterSelector formatters;
    private Lognit lognit;
    private UserConsole console;
    private DownloadRunner runner;
    private InOrder orderly;

    @Before
    public void setUp() throws Exception {
        timer = new SupportTimer();
        formatters = mock(FormatterSelector.class, RETURNS_DEEP_STUBS);
        lognit = mock(Lognit.class);
        console = mock(UserConsole.class);
        orderly = inOrder(formatters, lognit, console);
        runner = new DownloadRunner(console, lognit, formatters, timer);
    }

    @Test(timeout = 1000)
    public void whenDownloadingThreeBags() throws Exception {
        BlockingIterator<DownloadBag> iterator = bags(bag(10, "A", "B"), bag(5, "C", "D"), bag(0, "E", "F"));
        RestStream<DownloadBag> stream = stream(iterator);
        Formatter formatter = formatters.select("someformat");
        when(lognit.download("abc", 42)).thenReturn(stream);

        UserOptions options = new UserOptions("abc", "-n", "42", "-b", "someformat");
        Thread thread = runInAnotherThread(options);

        iterator.waitNext();

        timer.assertSchedule(0L, 1000L);
        orderly.verify(lognit).download("abc", 42);

        timer.runNextAt(0);
        orderly.verify(console).printStill(DownloadRunner.DOWNLOAD_STATUS, 0, 0, 0, 0.0);

        iterator.releaseAndWaitNext();
        verify(formatter).printMessage(msg("A"));
        verify(formatter).printMessage(msg("B"));
        timer.runNextAt(1000L);
        orderly.verify(console).printStill(DownloadRunner.DOWNLOAD_STATUS, 2, 2, 10, 0.0);

        iterator.releaseAndWaitNext();
        verify(formatter).printMessage(msg("C"));
        verify(formatter).printMessage(msg("D"));
        timer.runNextAt(2000L);
        orderly.verify(console).printStill(DownloadRunner.DOWNLOAD_STATUS, 4, 2, 5, 1.0);

        iterator.release();
        thread.join();
        verify(formatter).printMessage(msg("E"));
        verify(formatter).printMessage(msg("F"));

        timer.assertNoMoreTasks();
        orderly.verify(console).printStill(DownloadRunner.DOWNLOAD_STATUS, 6, 2, 0, 0.0);
    }

    private Thread runInAnotherThread(final UserOptions options) throws Exception {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    runner.run(options);
                } catch (Exception e) {
                    fail(e.getMessage());
                }
            }
        };
        thread.start();
        return thread;
    }

    private BlockingIterator<DownloadBag> bags(DownloadBag... bags) {
        return new BlockingIterator<DownloadBag>(Arrays.asList(bags).iterator());
    }

    private RestStream<DownloadBag> stream(Iterator<DownloadBag> iterator) {
        return spy(new RestStream<DownloadBag>(iterator, null));
    }

    private DownloadBag bag(int remaining, String... ids) {
        List<Message> messages = new ArrayList<Message>();
        for (String id : ids)
            messages.add(msg(id));
        return spy(new DownloadBag(messages, remaining));
    }

    private Message msg(String id) {
        return new Message(id);
    }
}
