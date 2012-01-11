package net.intelie.lognit.cli;

import net.intelie.lognit.cli.commands.Command;
import net.intelie.lognit.cli.state.StateKeeper;
import org.junit.Test;

import static org.fest.util.Arrays.array;
import static org.mockito.Mockito.*;

public class ArgsParserTest {
    @Test
    public void willRunCommandByItsName() throws Exception {
        StateKeeper keeper = mock(StateKeeper.class);

        Command cmd1 = mock(Command.class), cmd2 = mock(Command.class);
        when(cmd1.name()).thenReturn("abc");
        when(cmd2.name()).thenReturn("qwe");

        ArgsParser parser = new ArgsParser(null, keeper, cmd1, cmd2);
        reset(cmd1, cmd2);
        parser.run(array("qwe", "123", "456"));
        verify(keeper).begin();
        verify(keeper).end();

        verify(cmd2).execute("123", "456");
        verifyZeroInteractions(cmd1);
    }

    @Test
    public void wontRunIfNoCommandFound() throws Exception {
        StateKeeper keeper = mock(StateKeeper.class);
        UserInput console = mock(UserInput.class);

        Command cmd1 = mock(Command.class), cmd2 = mock(Command.class);
        when(cmd1.name()).thenReturn("abc");
        when(cmd2.name()).thenReturn("qwe");

        ArgsParser parser = new ArgsParser(console, keeper, cmd1, cmd2);
        reset(cmd1, cmd2);

        parser.run("aaa", "123", "456");
        verify(keeper).begin();
        verify(keeper).end();
        verify(console).printf(any(String.class));
        verify(console).printf(any(String.class), any(String.class));

        verifyZeroInteractions(cmd1, cmd2);
    }

    @Test
    public void wontRunIfNoCommandLineIsPassed() throws Exception {
        StateKeeper keeper = mock(StateKeeper.class);
        UserInput console = mock(UserInput.class);

        Command cmd1 = mock(Command.class), cmd2 = mock(Command.class);
        when(cmd1.name()).thenReturn("abc");
        when(cmd2.name()).thenReturn("qwe");

        ArgsParser parser = new ArgsParser(console, keeper, cmd1, cmd2);
        reset(cmd1, cmd2);

        parser.run();
        verify(keeper).begin();
        verify(keeper).end();
        verify(console).printf(any(String.class));
        verify(console).printf(any(String.class), any(String.class));

        verifyZeroInteractions(cmd1, cmd2);
    }

    @Test
    public void willRunAndNotRethrow() throws Exception {
        StateKeeper keeper = mock(StateKeeper.class);
        UserInput console = mock(UserInput.class);

        Command cmd1 = mock(Command.class);
        when(cmd1.name()).thenReturn("abc");
        doThrow(new RuntimeException()).when(cmd1).execute("123", "456");

        ArgsParser parser = new ArgsParser(console, keeper, cmd1);
        parser.run("abc", "123", "456");
        verify(keeper).begin();
        verify(keeper).end();
        verify(console).printf(anyString(), anyObject(), anyObject());

        verify(cmd1).execute(array("123", "456"));
    }
}
