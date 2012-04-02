package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.state.Clock;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SearchRunnerTest {
    private UserConsole console;
    private Lognit lognit;
    private SearchRunner runner;
    private BufferListenerFactory factory;
    private Clock clock;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        lognit = mock(Lognit.class, RETURNS_DEEP_STUBS);
        factory = mock(BufferListenerFactory.class, RETURNS_DEEP_STUBS);
        clock = mock(Clock.class);
        runner = new SearchRunner(console, lognit, factory, clock);
    }

    @Test
    public void whenHasQueryExecutesUsingCorrectFormatter() throws Exception {
        runner.run(new UserOptions("blablabla", "-n", "42", "-b", "plain"));
        BufferListener listener = factory.create("plain", false);
        verify(lognit).search("blablabla", 42, listener);
        verify(lognit.search("blablabla", 42, listener)).close();
    }



    @Test
    public void whenHasQueryExecutesSearchAndClose() throws Exception {
        runner.run(new UserOptions("blablabla", "-n", "42"));
        BufferListener listener = factory.create("colored", false);
        verify(lognit).search("blablabla", 42, listener);
        verify(lognit.search("blablabla", 42, listener)).close();
    }

    @Test
    public void whenHasQueryToFollowExecutesReleasesAndWait() throws Exception {
        runner.run(new UserOptions("blablabla", "-n", "42", "-f"));
        BufferListener listener = factory.create("colored", false);
        verify(lognit).search("blablabla", 42, listener);
        verify(listener).releaseAll();
        verify(clock).sleep(Integer.MAX_VALUE);
        verify(lognit.search("blablabla", 42, listener)).close();
    }

    @Test
    public void whenHasQueryToFollowExecutesReleasesAndWaitVerbosely() throws Exception {
        when(clock.currentMillis()).thenReturn(10L, 42L);
        runner.run(new UserOptions("blablabla", "-n", "42", "-f", "-v"));
        BufferListener listener = factory.create("colored", true);
        verify(lognit).search("blablabla", 42, listener);
        verify(console).println(SearchRunner.HANDSHAKE, 32L);
        verify(listener).releaseAll();
        verify(clock).sleep(Integer.MAX_VALUE);
        verify(lognit.search("blablabla", 42, listener)).close();
    }
}
