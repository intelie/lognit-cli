package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.json.Jsonizer;
import net.intelie.lognit.cli.model.Message;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JsonFormatterTest {
    private JsonFormatter printer;
    private UserConsole console;
    private Jsonizer jsonizer;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        jsonizer = mock(Jsonizer.class);
        printer = new JsonFormatter(console, jsonizer);
    }

    @Test
    public void testPrintMessage() throws Exception {
        Message message = new Message("123", "A", "11111111", "111111", "D", "E", "F", "abc", null);
        when(jsonizer.to(message)).thenReturn("abc");
        printer.printMessage(message);
        verify(console).printOut("%s", "abc");
    }

    @Test
    public void testPrintStatus() throws Exception {
        printer.printStatus("ABC", 1, "2", 3);
        verify(console).println("ABC", 1, "2", 3);
    }
}
