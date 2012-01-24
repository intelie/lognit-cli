package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.http.RestListenerHandle;
import net.intelie.lognit.cli.http.UnauthorizedException;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Welcome;
import org.apache.commons.httpclient.StatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class RequestRunnerTest {
    private UserConsole console;
    private Lognit lognit;
    private RequestRunner runner;
    private BufferListener listener;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        lognit = mock(Lognit.class, RETURNS_DEEP_STUBS);
        listener = mock(BufferListener.class);
        runner = new RequestRunner(console, lognit, listener);
    }

    @Test
    public void whenHasNoQueryOnlyPrintsWelcome() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        runner.run(new RequestOptions(null, null, null, "", null, null, false));
        verify(lognit).welcome();
        verify(console).println("blablabla");
        verifyNoMoreInteractions(console, lognit);
    }

    @Test
    public void whenHasQueryExecutesSearch() throws Exception {
        runner.run(new RequestOptions(null, null, null, "blablabla", null, null, false));
        verify(lognit).search("blablabla", 100, listener);
        verify(lognit.search("blablabla", 100, listener)).close();
    }

    @Test
    public void whenHasServerAlsoSetsTheServer() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        runner.run(new RequestOptions("someserver", null, null, "", null, null, false));

        verify(lognit).setServer("someserver");
        verify(lognit).welcome();
        verify(console).println("blablabla");
        verifyNoMoreInteractions(console, lognit);
    }

    @Test
    public void whenHasUserOnlyDoesNothingYet() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        runner.run(new RequestOptions(null, "user", null, "", null, null, false));

        verify(lognit).welcome();
        verify(console).println("blablabla");
        verifyNoMoreInteractions(console, lognit);
    }


    @Test
    public void whenHasUserAndPasswordAuthenticates() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        runner.run(new RequestOptions(null, "user", "pass", "", null, null, false));

        verify(lognit).authenticate("user", "pass");
        verify(lognit).welcome();
        verify(console).println("blablabla");
        verifyNoMoreInteractions(console, lognit);
    }

    @Test
    public void whenReceivesUnauthorizedTryAskUser() throws Exception {
        Welcome welcome = new Welcome("blablabla");
        when(lognit.welcome())
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")))
                .thenReturn(welcome);
        when(console.readLine("login: ")).thenReturn("somelogin");
        when(console.readPassword("%s's password: ", "somelogin")).thenReturn("somepass");

        runner.run(new RequestOptions(null, null, null, "", null, null, false));

        InOrder orderly = inOrder(lognit, console);
        orderly.verify(lognit).welcome();
        orderly.verify(console).println("HTTP/1.0 401 OK");
        orderly.verify(console).readLine(anyString());
        orderly.verify(console).readPassword(anyString(), anyString());
        orderly.verify(lognit).authenticate("somelogin", "somepass");
        orderly.verify(lognit).welcome();
        orderly.verify(console).println("blablabla");
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void whenReceivesUnauthorizedTryAskPasswordOnlyIfUserIsPassed() throws Exception {
        Welcome welcome = new Welcome("blablabla");
        when(lognit.welcome())
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")))
                .thenReturn(welcome);
        when(console.readPassword("%s's password: ", "somelogin")).thenReturn("somepass");

        runner.run(new RequestOptions(null, "somelogin", null, "", null, null, false));

        InOrder orderly = inOrder(lognit, console);
        orderly.verify(lognit).welcome();
        orderly.verify(console).println("HTTP/1.0 401 OK");
        orderly.verify(console).readPassword(anyString(), anyString());
        orderly.verify(lognit).authenticate("somelogin", "somepass");
        orderly.verify(lognit).welcome();
        orderly.verify(console).println("blablabla");
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void whenReceivesUnauthorizedAsksAtMostThreeTimes() throws Exception {
        when(lognit.welcome())
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")));
        when(console.readLine("login: ")).thenReturn("somelogin");
        when(console.readPassword("%s's password: ", "somelogin")).thenReturn("somepass");

        runner.run(new RequestOptions(null, null, null, "", null, null, false));

        verify(lognit, times(4)).welcome();
        verify(console, times(3)) .readLine(anyString());
    }

    @Test
    public void whenReceivesUnauthorizedAndHasUserAndPasswordTriesOnlyOnce() throws Exception {
        when(lognit.welcome())
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")));

        runner.run(new RequestOptions(null, "someuser", "somepass", "", null, null, false));

        verify(lognit).authenticate("someuser", "somepass");
        verify(lognit, times(1)).welcome();
        verify(console).println("HTTP/1.0 401 OK");
        verifyNoMoreInteractions(lognit, console);
    }

}
