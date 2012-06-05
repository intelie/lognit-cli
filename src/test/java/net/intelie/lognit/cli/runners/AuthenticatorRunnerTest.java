package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.http.UnauthorizedException;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.state.Clock;
import org.apache.commons.httpclient.StatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AuthenticatorRunnerTest {
    private UserConsole console;
    private Lognit lognit;
    private MainRunner main;
    private AuthenticatorRunner runner;
    private Clock clock;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        lognit = mock(Lognit.class, RETURNS_DEEP_STUBS);
        main = mock(MainRunner.class);
        clock = mock(Clock.class);
        runner = new AuthenticatorRunner(console, lognit, clock, main);
    }


    @Test
    public void whenHasServerAlsoSetsTheServer() throws Exception {
        UserOptions opts = new UserOptions("-s", "someserver");
        runner.run(opts);

        verify(lognit).setServer("someserver");
        verify(main).run(opts);
        verifyNoMoreInteractions(console, lognit);
    }

    @Test
    public void whenHasUserOnlyDoesNothingYet() throws Exception {
        UserOptions opts = new UserOptions("-u", "user");
        runner.run(opts);

        verify(main).run(opts);
        verifyNoMoreInteractions(console, lognit);
    }


    @Test
    public void whenHasUserAndPasswordAuthenticates() throws Exception {
        UserOptions opts = new UserOptions("-u", "user", "-p", "pass");
        runner.run(opts);

        verify(lognit).authenticate("user", "pass");
        verify(main).run(opts);
        verifyNoMoreInteractions(console, lognit);
    }


    @Test
    public void whenReceivesUnauthorizedTryAskUser() throws Exception {
        UserOptions opts = new UserOptions();

        when(main.run(opts))
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")))
                .thenReturn(0);
        when(console.readLine("login: ")).thenReturn("somelogin");
        when(console.readPassword("%s's password: ", "somelogin")).thenReturn("somepass");

        runner.run(opts);

        InOrder orderly = inOrder(lognit, console, main);
        orderly.verify(main).run(opts);
        orderly.verify(console).readLine(anyString());
        orderly.verify(console).readPassword(anyString(), anyString());
        orderly.verify(lognit).authenticate("somelogin", "somepass");
        orderly.verify(main).run(opts);
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void whenReceivesUnauthorizedTryAskPasswordOnlyIfUserIsPassed() throws Exception {
        UserOptions opts = new UserOptions("-u", "somelogin");

        when(main.run(opts))
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")))
                .thenReturn(0);
        when(console.readPassword("%s's password: ", "somelogin")).thenReturn("somepass");

        runner.run(opts);

        InOrder orderly = inOrder(lognit, console, main);
        orderly.verify(main).run(opts);
        orderly.verify(console).readPassword(anyString(), anyString());
        orderly.verify(lognit).authenticate("somelogin", "somepass");
        orderly.verify(main).run(opts);
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void whenReceivesUnauthorizedAsksAtMostThreeTimes() throws Exception {
        UserOptions opts = new UserOptions();
        when(main.run(opts))
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")));

        when(console.readLine("login: ")).thenReturn("somelogin");
        when(console.readPassword("%s's password: ", "somelogin")).thenReturn("somepass");

        assertThat(runner.run(opts)).isEqualTo(2);

        verify(main, times(4)).run(opts);
        verify(console, times(3)).readLine(anyString());
    }

    @Test
    public void whenReceivesUnauthorizedAndHasUserAndPasswordTriesOnlyOnce() throws Exception {
        UserOptions opts = new UserOptions("-u", "someuser", "-p", "somepass");
        when(main.run(opts))
                .thenThrow(new UnauthorizedException(new StatusLine("HTTP/1.0 401 OK")));

        runner.run(opts);

        verify(lognit, times(1)).getServer();
        verify(lognit).authenticate("someuser", "somepass");
        verify(main, times(1)).run(opts);
        verify(console).println("(%s): %s", null, "HTTP/1.0 401 OK");
        verifyNoMoreInteractions(lognit, console);
    }

    @Test
    public void whenReceivesUnauthorizedAndHasUserAndPasswordButExceptionIsRetryTriesForever() throws Exception {
        UserOptions opts = new UserOptions("-u", "someuser", "-p", "somepass");
        when(main.run(any(UserOptions.class)))
                .thenThrow(new RetryConnectionException(new UserOptions("abc"), "qwe"))
                .thenThrow(new RetryConnectionException(new UserOptions("abc2"), "qwe2"))
                .thenReturn(0);

        runner.run(opts);

        verify(lognit, times(2)).getServer();
        verify(lognit).authenticate("someuser", "somepass");
        verify(main, times(1)).run(opts);
        verify(main, times(1)).run(new UserOptions("abc"));
        verify(main, times(1)).run(new UserOptions("abc2"));
        verify(clock, times(2)).sleep(2000);
        verify(console).println("(%s): %s", null, "qwe");
        verify(console).println("(%s): %s", null, "qwe2");
        verifyNoMoreInteractions(lognit, console, clock);
    }

}
