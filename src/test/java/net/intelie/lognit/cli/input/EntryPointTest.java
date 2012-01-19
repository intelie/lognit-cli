package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.state.StateKeeper;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.mockito.InOrder;

import static org.fest.util.Arrays.array;
import static org.mockito.Mockito.*;

public class EntryPointTest {

    private UserOptions options;
    private UserInput console;
    private StateKeeper state;
    private EntryPoint entry;
    private InOrder orderly;

    @Before
    public void setUp() throws Exception {
        options = mock(UserOptions.class);
        console = mock(UserInput.class);
        state = mock(StateKeeper.class);
        entry = new EntryPoint(console, options, state);
        orderly = inOrder(options,  console, state);
    }

    @Test
    public void willRunOptions() {
        entry.run();

        orderly.verify(state).begin();
        orderly.verify(options).run();
        orderly.verify(state).end();
        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void willCatchExceptions() {
        doThrow(new RuntimeException("abc")).when(options).run();

        entry.run();

        orderly.verify(state).begin();
        orderly.verify(options).run();
        orderly.verify(console).println("%s: %s", "RuntimeException", "abc");
        orderly.verify(state).end();
        orderly.verifyNoMoreInteractions();
    }


}
