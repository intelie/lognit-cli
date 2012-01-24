package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.model.Message;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MessagePrinterTest {

    private MessagePrinter printer;
    private UserConsole console;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        printer = new MessagePrinter(console);
    }

    @Test
    public void testPrintStatus() throws Exception {
        Message message = new Message("abc", "qwe");
        printer.printMessage(message);
        verify(console).printOut("qwe");
    }

    @Test
    public void testPrintMessage() throws Exception {
        printer.printStatus("ABC", 1, "2", 3);
        verify(console).println("ABC", 1, "2", 3);
    }
}
