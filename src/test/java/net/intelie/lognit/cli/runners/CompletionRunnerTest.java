package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Terms;
import net.intelie.lognit.cli.state.Clock;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CompletionRunnerTest {
    private UserConsole console;
    private Lognit lognit;
    private CompletionRunner runner;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        lognit = mock(Lognit.class, RETURNS_DEEP_STUBS);
        runner = new CompletionRunner(console, lognit);
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
}
