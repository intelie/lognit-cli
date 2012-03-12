package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.UserConsole;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UsageRunnerTest {

    private UserConsole console;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
    }

    @Test
    public void willPrintToConsole() {
        UsageRunner printer = new UsageRunner(console);
        assertThat(printer.run(null)).isEqualTo(0);
        verify(console).println(anyString());
    }

    @Test
    public void willPrintToConsoleWithException() {
        UserConsole console = mock(UserConsole.class);
        doThrow(new RuntimeException()).when(console).println(anyString());
        UsageRunner printer = new UsageRunner(console);
        printer.run(null);
        verify(console).println(anyString());
    }
}
