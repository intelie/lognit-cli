package net.intelie.lognit.cli.input;

import org.junit.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UsageRunnerTest {
    @Test
    public void willPrintToConsole() {
        UserConsole console = mock(UserConsole.class);
        UsageRunner printer = new UsageRunner(console);
        printer.run();
        verify(console).println(anyString());
    }

    @Test
    public void willPrintToConsoleWithException() {
        UserConsole console = mock(UserConsole.class);
        doThrow(new RuntimeException()).when(console).println(anyString());
        UsageRunner printer = new UsageRunner(console);
        printer.run();
        verify(console).println(anyString());
    }
}
