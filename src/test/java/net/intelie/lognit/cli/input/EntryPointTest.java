package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.state.StateKeeper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class EntryPointTest {

    private UserOptions options;
    private UserConsole console;
    private StateKeeper state;
    private EntryPoint entry;
    private InOrder orderly;

    @Before
    public void setUp() throws Exception {
        options = mock(UserOptions.class);
        console = mock(UserConsole.class);
        state = mock(StateKeeper.class);
        entry = new EntryPoint(console, options, state);
        orderly = inOrder(options, console, state);
    }

    @Test
    public void willRunOptions() throws Exception {
        entry.run("-a", "-b", "c");

        orderly.verify(state).begin();
        orderly.verify(options).run("-a", "-b", "c");
        orderly.verify(state).end();
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void willCatchExceptions() throws Exception {
        doThrow(new RuntimeException("abc")).when(options).run("-a", "-b", "c");

        entry.run("-a", "-b", "c");

        orderly.verify(state).begin();
        orderly.verify(options).run("-a", "-b", "c");
        orderly.verify(console).println("%s: %s", "RuntimeException", "abc");
        orderly.verify(state).end();
        orderly.verifyNoMoreInteractions();
    }


}
