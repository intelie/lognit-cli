package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.http.RestListener;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.state.Clock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SearchRunnerTest {
    private UserConsole console;
    private Lognit lognit;
    private SearchRunner runner;
    private BufferListenerFactory factory;
    private Clock clock;
    private Runtime runtime;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        lognit = mock(Lognit.class, RETURNS_DEEP_STUBS);
        factory = mock(BufferListenerFactory.class, RETURNS_DEEP_STUBS);
        clock = mock(Clock.class);
        runtime = mock(Runtime.class);
        runner = new SearchRunner(console, lognit, factory, clock, runtime);
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
    public void whenHasQueryToFollowButHandshakeFails() throws Exception {
        try {
            when(lognit.search(eq("blablabla"), eq(42), any(RestListener.class))).thenThrow(new RuntimeException());
            runner.run(new UserOptions("blablabla", "-n", "42", "-f"));
            fail("must throw");
        } catch (RetryConnectionException e) {
            assertThat(e.options()).isEqualTo(new UserOptions("blablabla", "-n", "42", "-f"));
        }
    }

    @Test
    public void whenHasQueryToFollowExecutesReleasesAndWait() throws Exception {
        try {
            runner.run(new UserOptions("blablabla", "-n", "42", "-f"));
            fail("must throw");
        } catch (RetryConnectionException e) {
            assertThat(e.options()).isEqualTo(new UserOptions("blablabla", "-n", "0", "-f"));
            BufferListener listener = factory.create("colored", false);
            verify(lognit).search("blablabla", 42, listener);
            verify(listener).releaseAll();
            verify(lognit.search("blablabla", 42, listener)).waitDisconnected();
            verify(lognit.search("blablabla", 42, listener)).close();
        }
    }

    @Test
    public void whenHasQueryToFollowRegisterShutdown() throws Exception {
        try {
            runner.run(new UserOptions("blablabla", "-n", "42", "-f"));
            fail("must throw");
        } catch (RetryConnectionException e) {
            assertThat(e.options()).isEqualTo(new UserOptions("blablabla", "-n", "0", "-f"));
            BufferListener listener = factory.create("colored", false);
            verify(lognit).search("blablabla", 42, listener);
            verify(listener).releaseAll();

            ArgumentCaptor<Thread> captor = ArgumentCaptor.forClass(Thread.class);
            verify(runtime).addShutdownHook(captor.capture());

            captor.getValue().run();
            verify(lognit.search("blablabla", 42, listener)).waitDisconnected();
            verify(lognit.search("blablabla", 42, listener), times(2)).close();
        }
    }

    @Test
    public void whenHasQueryToFollowExecutesReleasesAndWaitVerbosely() throws Exception {
        try {
            when(clock.currentMillis()).thenReturn(10L, 42L);
            runner.run(new UserOptions("blablabla", "-n", "42", "-f", "-v"));
            fail("must throw");
        } catch (RetryConnectionException e) {
            assertThat(e.options()).isEqualTo(new UserOptions("blablabla", "-n", "0", "-f", "-v"));
            BufferListener listener = factory.create("colored", true);
            verify(lognit).search("blablabla", 42, listener);
            verify(console).println(SearchRunner.HANDSHAKE, 32L);
            verify(listener).releaseAll();
            verify(lognit.search("blablabla", 42, listener)).waitDisconnected();
            verify(lognit.search("blablabla", 42, listener)).close();
        }
    }
}
