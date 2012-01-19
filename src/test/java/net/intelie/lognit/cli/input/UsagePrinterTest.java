package net.intelie.lognit.cli.input;

import org.junit.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UsagePrinterTest {
    @Test
    public void willPrintToConsole() {
        UserInput console = mock(UserInput.class);
        UsagePrinter printer = new UsagePrinter(console);
        printer.run();
        verify(console).println(anyString());
    }

    @Test
    public void willPrintToConsoleWithException() {
        UserInput console = mock(UserInput.class);
        doThrow(new RuntimeException()).when(console).println(anyString());
        UsagePrinter printer = new UsagePrinter(console);
        printer.run();
        verify(console).println(anyString());
    }
}
