package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.http.UnauthorizedException;
import net.intelie.lognit.cli.model.*;
import net.intelie.lognit.cli.state.Clock;
import org.apache.commons.httpclient.StatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RequestRunnerTest {
    private UserConsole console;
    private Lognit lognit;
    private RequestRunner runner;
    private BufferListenerFactory factory;
    private Clock clock;
    private InfoRunner infoRunner;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        lognit = mock(Lognit.class, RETURNS_DEEP_STUBS);
        factory = mock(BufferListenerFactory.class, RETURNS_DEEP_STUBS);
        infoRunner = mock(InfoRunner.class);
        clock = mock(Clock.class);
        runner = new RequestRunner(console, lognit, infoRunner, factory, clock);
    }

    @Test
    public void whenHasNoQueryShowsWelcome() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        assertThat(runner.run(new UserOptions())).isEqualTo(0);
        verify(lognit).getServer();
        verify(lognit).welcome();
        verify(console).println("(%s): %s", null, "blablabla");
        verifyNoMoreInteractions(console, lognit);
    }

    @Test
    public void whenHasInfoPrintsInfo() throws Exception {
        StatsSummary summary = mock(StatsSummary.class);

        when(lognit.getServer()).thenReturn("someserver");
        when(lognit.stats()).thenReturn(summary);

        runner.run(new UserOptions("-i"));
        
        verify(infoRunner).printInfo("someserver", summary);
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
        verify(console).waitChar('q');
        verify(lognit.search("blablabla", 42, listener)).close();
    }

    @Test
    public void whenHasQueryToFollowExecutesReleasesAndWaitVerbosely() throws Exception {
        when(clock.currentMillis()).thenReturn(10L, 42L);
        runner.run(new UserOptions("blablabla", "-n", "42", "-f", "-v"));
        BufferListener listener = factory.create("colored", true);
        verify(lognit).search("blablabla", 42, listener);
        verify(console).println(RequestRunner.HANDSHAKE, 32L);
        verify(listener).releaseAll();
        verify(console).waitChar('q');
        verify(lognit.search("blablabla", 42, listener)).close();
    }

    @Test
    public void whenHasServerAlsoSetsTheServer() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        when(lognit.getServer()).thenReturn("someserver");
        runner.run(new UserOptions("-s", "someserver"));

        verify(lognit).setServer("someserver");
        verify(lognit).getServer();
        verify(lognit).welcome();
        verify(console).println("(%s): %s", "someserver", "blablabla");
        verifyNoMoreInteractions(console, lognit);
    }

    @Test
    public void whenHasUserOnlyDoesNothingYet() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        runner.run(new UserOptions("-u", "user"));

        verify(lognit).getServer();
        verify(lognit).welcome();
        verify(console).println("(%s): %s", null, "blablabla");
        verifyNoMoreInteractions(console, lognit);
    }


    @Test
    public void whenHasUserAndPasswordAuthenticates() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        when(lognit.getServer()).thenReturn("someserver");
        runner.run(new UserOptions("-u", "user", "-p", "pass"));

        verify(lognit).authenticate("user", "pass");
        verify(lognit).getServer();
        verify(lognit).welcome();
        verify(console).println("(%s): %s", "someserver", "blablabla");
        verifyNoMoreInteractions(console, lognit);
    }

    @Test
    public void whenIsCompletePrintTerms() throws Exception {
        when(lognit.terms("", "aaa")).thenReturn(new Terms(Arrays.asList("aaab", "aaac", "aaad")));
        runner.run(new UserOptions("-c", "aaa"));
        verify(console).printOut("aaab");
        verify(console).printOut("aaac");
        verify(console).printOut("aaad");
    }

    @Test
    public void whenIsCompleteWithColonPrintTerms() throws Exception {
        when(lognit.terms("aaa", "bbb ccc")).thenReturn(new Terms(Arrays.asList("aaab", "aaac", "aaad")));
        runner.run(new UserOptions("-c", "aaa:bbb", "ccc"));
        verify(console).printOut("aaab");
        verify(console).printOut("aaac");
        verify(console).printOut("aaad");
    }

    @Test
    public void whenIsCompleteWithColonOnlyPrintTerms() throws Exception {
        when(lognit.terms("aaa", "")).thenReturn(new Terms(Arrays.asList("aaab", "aaac", "aaad")));
        runner.run(new UserOptions("-c", "aaa:"));
        verify(console).printOut("aaab");
        verify(console).printOut("aaac");
        verify(console).printOut("aaad");
    }

    @Test
    public void whenIsCompleteEmptyCallsWithNothing() throws Exception {
        when(lognit.terms("", "")).thenReturn(new Terms(Arrays.asList("aaab", "aaac", "aaad")));
        runner.run(new UserOptions("-c", ""));
        verify(console).printOut("aaab");
        verify(console).printOut("aaac");
        verify(console).printOut("aaad");
    }

    @Test
    public void whenReceivesUnauthorizedTryAskUser() throws Exception {
        Welcome welcome = new Welcome("blablabla");
        when(lognit.getServer()).thenReturn("someserver");
        when(lognit.welcome())
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")))
                .thenReturn(welcome);
        when(console.readLine("login: ")).thenReturn("somelogin");
        when(console.readPassword("%s's password: ", "somelogin")).thenReturn("somepass");

        runner.run(new UserOptions());

        InOrder orderly = inOrder(lognit, console);
        orderly.verify(lognit).getServer();
        orderly.verify(lognit).welcome();
        orderly.verify(console).println("(%s): %s", "someserver", "HTTP/1.0 401 OK");
        orderly.verify(console).readLine(anyString());
        orderly.verify(console).readPassword(anyString(), anyString());
        orderly.verify(lognit).authenticate("somelogin", "somepass");
        orderly.verify(lognit).getServer();
        orderly.verify(lognit).welcome();
        orderly.verify(console).println("(%s): %s", "someserver", "blablabla");
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void whenReceivesUnauthorizedTryAskPasswordOnlyIfUserIsPassed() throws Exception {
        Welcome welcome = new Welcome("blablabla");
        when(lognit.getServer()).thenReturn("someserver");
        when(lognit.welcome())
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")))
                .thenReturn(welcome);
        when(console.readPassword("%s's password: ", "somelogin")).thenReturn("somepass");

        runner.run(new UserOptions("-u", "somelogin"));

        InOrder orderly = inOrder(lognit, console);
        orderly.verify(lognit).getServer();
        orderly.verify(lognit).welcome();
        orderly.verify(console).println("(%s): %s", "someserver", "HTTP/1.0 401 OK");
        orderly.verify(console).readPassword(anyString(), anyString());
        orderly.verify(lognit).authenticate("somelogin", "somepass");
        orderly.verify(lognit).getServer();
        orderly.verify(lognit).welcome();
        orderly.verify(console).println("(%s): %s", "someserver", "blablabla");
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void whenReceivesUnauthorizedAsksAtMostThreeTimes() throws Exception {
        when(lognit.welcome())
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")));
        when(console.readLine("login: ")).thenReturn("somelogin");
        when(console.readPassword("%s's password: ", "somelogin")).thenReturn("somepass");

        assertThat(runner.run(new UserOptions())).isEqualTo(2);

        verify(lognit, times(4)).welcome();
        verify(console, times(3)).readLine(anyString());
    }

    @Test
    public void whenReceivesUnauthorizedAndHasUserAndPasswordTriesOnlyOnce() throws Exception {
        when(lognit.welcome())
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")));

        runner.run(new UserOptions("-u", "someuser", "-p", "somepass"));

        verify(lognit, times(2)).getServer();
        verify(lognit).authenticate("someuser", "somepass");
        verify(lognit, times(1)).welcome();
        verify(console).println("(%s): %s", null, "HTTP/1.0 401 OK");
        verifyNoMoreInteractions(lognit, console);
    }

}
