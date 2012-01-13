package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.state.StateKeeper;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import static org.fest.util.Arrays.array;
import static org.mockito.Mockito.*;

public class EntryPointTest {
    @Test
    public void willRunCommandByItsName() throws Exception {
        StateKeeper keeper = mock(StateKeeper.class);

        Command cmd1 = mock(Command.class), cmd2 = mock(Command.class);
        when(cmd1.name()).thenReturn("abc");
        when(cmd2.name()).thenReturn("qwe");

        EntryPoint parser = new EntryPoint(null, keeper, cmd1, cmd2);
        reset(cmd1, cmd2);
        parser.run(array("qwe", "123", "456"));
        verify(keeper).begin();
        verify(keeper).end();

        verify(cmd2).execute(new ArgsParser("123", "456"));
        verifyZeroInteractions(cmd1);
    }

    @Test
    public void whenComandNotFound() throws Exception {
        StateKeeper keeper = mock(StateKeeper.class);
        UserInput console = mock(UserInput.class);

        EntryPoint parser = new EntryPoint(console, keeper);

        parser.run("aaa", "123", "456");
        verify(keeper).begin();
        verify(keeper).end();
        verify(console, times(2)).println(any(String.class));
    }


    @Test
    public void wontRunIfNoCommandLineIsPassed() throws Exception {
        StateKeeper keeper = mock(StateKeeper.class);
        UserInput console = mock(UserInput.class);

        Command cmd1 = mock(Command.class), cmd2 = mock(Command.class);
        when(cmd1.name()).thenReturn("abc");
        when(cmd2.name()).thenReturn("qwe");

        EntryPoint parser = new EntryPoint(console, keeper, cmd1, cmd2);
        reset(cmd1, cmd2);

        parser.run();
        verify(keeper).begin();
        verify(keeper).end();
        verify(console, times(2)).println(any(String.class));

        verifyZeroInteractions(cmd1, cmd2);
    }

    @Test
    public void willRunAndNotRethrow() throws Exception {
        StateKeeper keeper = mock(StateKeeper.class);
        UserInput console = mock(UserInput.class);

        Command cmd1 = mock(Command.class);
        when(cmd1.name()).thenReturn("abc");
        doThrow(new RuntimeException()).when(cmd1).execute(new ArgsParser("123", "456"));

        EntryPoint parser = new EntryPoint(console, keeper, cmd1);
        parser.run("abc", "123", "456");
        verify(keeper).begin();
        verify(keeper).end();
        verify(console).println(anyString(), anyObject(), anyObject());

        verify(cmd1).execute(new ArgsParser("123", "456"));
    }

    @Test
    public void whenPrintingUsageWithExceptionOnConsole() throws Exception {
        StateKeeper keeper = mock(StateKeeper.class);
        UserInput console = mock(UserInput.class);
        doThrow(new RuntimeException()).when(console).println(argThat(new TypeSafeMatcher<String>() {
            @Override
            public boolean matchesSafely(String s) {
                return s.contains("\n");
            }

            @Override
            public void describeTo(Description description) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        }));
        
        EntryPoint parser = new EntryPoint(console, keeper);
        parser.run();
    }
}
