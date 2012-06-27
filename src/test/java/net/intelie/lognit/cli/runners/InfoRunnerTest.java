package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Stats;
import net.intelie.lognit.cli.model.StatsSummary;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class InfoRunnerTest {

    private UserConsole console;
    private InfoRunner runner;
    private Lognit lognit;
    private InOrder orderly;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        lognit = mock(Lognit.class);
        orderly = inOrder(console);
        runner = new InfoRunner(console, lognit);
    }

    @Test
    public void whenHasInfoPrintsInfoWhenAllResponded() throws Exception {
        StatsSummary summary = new StatsSummary(150, 450, 42,
                Arrays.asList("AA", "BB"),
                Arrays.asList("AAA", "BBB", "CCC", "DDD"),
                Arrays.asList(
                        new Stats("AA", 100, 300, 28, Arrays.asList("AAA", "BBB", "CCC"), null, null),
                        new Stats("BB", 50, 150, 14, Arrays.asList("DDD", "BBB", "CCC"), null, null)),
                null, null, 0);
        when(lognit.stats()).thenReturn(summary);
        when(lognit.getServer()).thenReturn("someserver");
        runner.run(null);
        
        orderly.verify(console).printOut(InfoRunner.NO_MISSING_NODES, "someserver");

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_TEXT, "AA");
        orderly.verify(console).printOut(InfoRunner.INFO_TEXT, 3, 100L, 300L/1024.0/1024, 28L);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_TEXT, "BB");
        orderly.verify(console).printOut(InfoRunner.INFO_TEXT, 3, 50L, 150L/1024.0/1024, 14L);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.SUMMARY_TEXT);
        orderly.verify(console).printOut(InfoRunner.INFO_TEXT, 4, 150L, 450L/1024.0/1024, 42L);
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void whenHasInfoPrintsInfoWhenSomeDidNotRespond() throws Exception {
        StatsSummary summary = new StatsSummary(150, 450, 42,
                Arrays.asList("AA", "BB"),
                Arrays.asList("AAA", "BBB", "CCC", "DDD"),
                Arrays.asList(
                        new Stats("AA", 100, 300, 28, Arrays.asList("AAA", "BBB", "CCC"), null, null),
                        new Stats("BB", 50, 150, 14, Arrays.asList("DDD", "BBB", "CCC"), null, null)),
                null, null, 2);

        when(lognit.stats()).thenReturn(summary);
        when(lognit.getServer()).thenReturn("someserver");
        runner.run(null);

        orderly.verify(console).printOut(InfoRunner.HAS_MISSING_NODES, "someserver", 2);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_TEXT, "AA");
        orderly.verify(console).printOut(InfoRunner.INFO_TEXT, 3, 100L, 300L/1024.0/1024, 28L);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_TEXT, "BB");
        orderly.verify(console).printOut(InfoRunner.INFO_TEXT, 3, 50L, 150L/1024.0/1024, 14L);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.SUMMARY_TEXT);
        orderly.verify(console).printOut(InfoRunner.INFO_TEXT, 4, 150L, 450L/1024.0/1024, 42L);
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void whenHasInfoPrintsInfoWhenHasLoad() throws Exception {
        StatsSummary summary = new StatsSummary(150, 450, 42,
                Arrays.asList("AA", "BB"),
                Arrays.asList("AAA", "BBB", "CCC", "DDD"),
                Arrays.asList(
                        new Stats("AA", 100, 300, 28, Arrays.asList("AAA", "BBB", "CCC"),
                                Arrays.asList(1L, 2L, 3L, 114L), Arrays.asList(1L*1024*1024, 2L*1024*1024, 3L*1024*1024, 114L*1024*1024)),
                        new Stats("BB", 50, 150, 14, Arrays.asList("DDD", "BBB", "CCC"),
                                Arrays.asList(1L, 2L, 3L, 114L), Arrays.asList(1L*1024*1024, 2L*1024*1024, 3L*1024*1024, 114L*1024*1024))),
                Arrays.asList(2L, 4L, 6L, 228L), Arrays.asList(2L*1024*1024, 4L*1024*1024, 6L*1024*1024, 228L*1024*1024), 0);

        when(lognit.stats()).thenReturn(summary);
        when(lognit.getServer()).thenReturn("someserver");
        runner.run(null);

        orderly.verify(console).printOut(InfoRunner.NO_MISSING_NODES, "someserver");

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_TEXT, "AA");
        orderly.verify(console).printOut(InfoRunner.INFO_TEXT, 3, 100L, 300L/1024.0/1024, 28L);
        orderly.verify(console).printOut(InfoRunner.DOCS_TEXT, 2L, 8L, 2L);
        orderly.verify(console).printOut(InfoRunner.BYTES_TEXT, 2.0, 8.0, 2.0);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_TEXT, "BB");
        orderly.verify(console).printOut(InfoRunner.DOCS_TEXT, 2L, 8L, 2L);
        orderly.verify(console).printOut(InfoRunner.BYTES_TEXT, 2.0, 8.0, 2.0);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.SUMMARY_TEXT);
        orderly.verify(console).printOut(InfoRunner.DOCS_TEXT, 4L, 16L, 4L);
        orderly.verify(console).printOut(InfoRunner.BYTES_TEXT, 4.0, 16.0, 4.0);
        orderly.verifyNoMoreInteractions();
    }

}
