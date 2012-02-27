package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.input.UserConsole;
import net.intelie.lognit.cli.model.Message;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlainFormatterTest {
    private PlainFormatter printer;
    private UserConsole console;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        printer = new PlainFormatter(console);
    }

    @Test
    public void testPrintMessage() throws Exception {
        Message message = new Message("123", "A", "11111111", "111111", "D", "E", "F", "abc", null);
        printer.printMessage(message);
        verify(console).printOut("%s %s %s %s %s %s", "A", "Nov 11 11:11:11", "D", "E", "F", "abc");
    }

    @Test
    public void testPrintStatus() throws Exception {
        printer.printStatus("ABC", 1, "2", 3);
        verify(console).println("ABC", 1, "2", 3);
    }
}
