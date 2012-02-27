package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.input.UserConsole;
import net.intelie.lognit.cli.model.Message;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ColoredFormatterTest {

    private ColoredFormatter printer;
    private UserConsole console;

    private static final String YELLOW="\033[33m";
    private static final String GREEN="\033[32m";
    private static final String CYAN="\033[36m";
    private static final String NONE="\033[0m";

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        printer = new ColoredFormatter(console);
    }

    @Test
    public void testPrintStatus() throws Exception {
        Message message = new Message("123", "A", "11111111", "111111", "D", "E", "F", "abc");
        printer.printMessage(message);
        verify(console).printOut("$cA$n $gNov 11 11:11:11$n D E $yF$n abc"
                .replace("$c", CYAN).replace("$g", GREEN).replace("$y", YELLOW).replace("$n", NONE));
    }

    @Test
    public void testPrintMessage() throws Exception {
        printer.printStatus("ABC", 1, "2", 3);
        verify(console).println("ABC", 1, "2", 3);
    }
}
