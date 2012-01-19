package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.http.UnauthorizedException;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Welcome;
import org.apache.commons.httpclient.StatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class RequestRunnerTest {
    private UserInput console;
    private Lognit lognit;
    private RequestRunner runner;

    @Before
    public void setUp() throws Exception {
        console = mock(UserInput.class);
        lognit = mock(Lognit.class);
        runner = new RequestRunner(console, lognit);
    }

    @Test
    public void whenHasNoQueryOnlyPrintsWelcome() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        runner.run(null, null, null, "");
        verify(lognit).welcome();
        verify(console).println("blablabla");
        verifyNoMoreInteractions(console, lognit);
    }

    @Test
    public void whenHasServerAlsoSetsTheServer() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        runner.run("someserver", null, null, "");

        verify(lognit).setServer("someserver");
        verify(lognit).welcome();
        verify(console).println("blablabla");
        verifyNoMoreInteractions(console, lognit);
    }

    @Test
    public void whenHasUserOnlyDoesNothingYet() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        runner.run(null, "user", null, "");

        verify(lognit).welcome();
        verify(console).println("blablabla");
        verifyNoMoreInteractions(console, lognit);
    }


    @Test
    public void whenHasUserAndPasswordAuthenticates() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        runner.run(null, "user", "pass", "");

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

        runner.run(null, null, null, "");

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

        runner.run(null, "somelogin", null, "");

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

        runner.run(null, null, null, "");

        verify(lognit, times(4)).welcome();
        verify(console, times(3)) .readLine(anyString());
    }

    @Test
    public void whenReceivesUnauthorizedAndHasUserAndPasswordTriesOnlyOnce() throws Exception {
        when(lognit.welcome())
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")));

        runner.run(null, "someuser", "somepass", "");

        verify(lognit).authenticate("someuser", "somepass");
        verify(lognit, times(1)).welcome();
        verify(console).println("HTTP/1.0 401 OK");
        verifyNoMoreInteractions(lognit, console);
    }

}
