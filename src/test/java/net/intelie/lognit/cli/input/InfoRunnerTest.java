package net.intelie.lognit.cli.input;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.intelie.lognit.cli.model.Stats;
import net.intelie.lognit.cli.model.StatsSummary;
import org.apache.commons.lang.math.Range;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class InfoRunnerTest {

    private UserConsole console;
    private InfoRunner runner;
    private InOrder orderly;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        orderly = inOrder(console);
        runner = new InfoRunner(console);
    }

    @Test
    public void whenHasInfoPrintsInfoWhenAllResponded() throws Exception {
        StatsSummary summary = new StatsSummary(150,
                Arrays.asList("AA", "BB"),
                Arrays.asList("AAA", "BBB", "CCC", "DDD"),
                Arrays.asList(
                        new Stats("AA", 100, Arrays.asList("AAA", "BBB", "CCC"), null),
                        new Stats("BB", 50, Arrays.asList("DDD", "BBB", "CCC"), null)),
                null, 0);

        runner.printInfo("someserver", summary);

        orderly.verify(console).printOut(InfoRunner.NO_MISSING_NODES, "someserver");

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_INFO, "AA");
        orderly.verify(console).printOut(InfoRunner.STATS_INFO, 3, 100L);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_INFO, "BB");
        orderly.verify(console).printOut(InfoRunner.STATS_INFO, 3, 50L);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.TOTAL_INFO);
        orderly.verify(console).printOut(InfoRunner.STATS_INFO, 4, 150L);
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void whenHasInfoPrintsInfoWhenSomeDidNotRespond() throws Exception {
        StatsSummary summary = new StatsSummary(150,
                Arrays.asList("AA", "BB"),
                Arrays.asList("AAA", "BBB", "CCC", "DDD"),
                Arrays.asList(
                        new Stats("AA", 100, Arrays.asList("AAA", "BBB", "CCC"), null),
                        new Stats("BB", 50, Arrays.asList("DDD", "BBB", "CCC"), null)),
                null, 2);

        runner.printInfo("someserver", summary);

        orderly.verify(console).printOut(InfoRunner.HAS_MISSING_NODES, "someserver", 2);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_INFO, "AA");
        orderly.verify(console).printOut(InfoRunner.STATS_INFO, 3, 100L);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_INFO, "BB");
        orderly.verify(console).printOut(InfoRunner.STATS_INFO, 3, 50L);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.TOTAL_INFO);
        orderly.verify(console).printOut(InfoRunner.STATS_INFO, 4, 150L);
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void whenHasInfoPrintsInfoWhenHasLoad() throws Exception {
        StatsSummary summary = new StatsSummary(150,
                Arrays.asList("AA", "BB"),
                Arrays.asList("AAA", "BBB", "CCC", "DDD"),
                Arrays.asList(
                        new Stats("AA", 100, Arrays.asList("AAA", "BBB", "CCC"), Arrays.asList(1L, 2L, 3L, 114L)),
                        new Stats("BB", 50, Arrays.asList("DDD", "BBB", "CCC"), Arrays.asList(1L, 2L, 3L, 114L))),
                Arrays.asList(2L, 4L, 6L, 228L), 0);

        runner.printInfo("someserver", summary);

        orderly.verify(console).printOut(InfoRunner.NO_MISSING_NODES, "someserver");

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_INFO, "AA");
        orderly.verify(console).printOut(InfoRunner.STATS_INFO, 3, 100L);
        orderly.verify(console).printOut(InfoRunner.LOAD_INFO, 2L, 8L, 2L);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.NODE_INFO, "BB");
        orderly.verify(console).printOut(InfoRunner.LOAD_INFO, 2L, 8L, 2L);

        orderly.verify(console).printOut("");
        orderly.verify(console).printOut(InfoRunner.TOTAL_INFO);
        orderly.verify(console).printOut(InfoRunner.LOAD_INFO, 4L, 16L, 4L);
        orderly.verifyNoMoreInteractions();
    }

}
