package net.intelie.lognit.cli;

import net.intelie.lognit.cli.runners.AuthenticatorRunner;
import net.intelie.lognit.cli.state.StateKeeper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class EntryPointTest {
    private UserConsole console;
    private StateKeeper state;
    private EntryPoint entry;
    private AuthenticatorRunner request;
    private InOrder orderly;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        state = mock(StateKeeper.class);
        request = mock(AuthenticatorRunner.class);
        entry = new EntryPoint(console, state, request);
        orderly = inOrder(console, state, request);
    }

    @Test
    public void willRunOptions() throws Exception {
        assertThat(entry.run("-a", "-b", "c")).isEqualTo(0);

        orderly.verify(state).begin();
        orderly.verify(request).run(new UserOptions("-a", "-b", "c"));
        orderly.verify(state).end();
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void willRunRequestWithoutQuery() throws Exception {
        entry.run();

        orderly.verify(state).begin();
        orderly.verify(request).run(new UserOptions());
        orderly.verify(state).end();
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void willCatchExceptions() throws Exception {
        doThrow(new RuntimeException("abc")).when(request).run(new UserOptions("-a", "-b", "c"));

        assertThat(entry.run("-a", "-b", "c")).isEqualTo(3);

        orderly.verify(state).begin();
        orderly.verify(request).run(new UserOptions("-a", "-b", "c"));
        orderly.verify(console).fixCursor();
        orderly.verify(console).println("%s: %s", "RuntimeException", "abc");
        orderly.verify(console).println(anyString());
        orderly.verify(state).end();
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void willCatchExceptionsInVerboseMode() throws Exception {
        doThrow(new RuntimeException("abc")).when(request).run(new UserOptions("-a", "-b", "c", "-v"));

        assertThat(entry.run("-a", "-b", "c", "-v")).isEqualTo(3);

        orderly.verify(state).begin();
        orderly.verify(request).run(new UserOptions("-a", "-b", "c", "-v"));
        orderly.verify(console).fixCursor();
        orderly.verify(console).println("%s: %s", "RuntimeException", "abc");
        orderly.verify(console).println(anyString());
        orderly.verify(state).end();
        orderly.verifyNoMoreInteractions();
    }


}
