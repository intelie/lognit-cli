package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.AggregatedItemHelper;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.AggregatedItem;
import net.intelie.lognit.cli.model.Message;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ColoredFormatterTest {

    private ColoredFormatter printer;
    private UserConsole console;

    private static final String YELLOW = "\033[33m";
    private static final String GREEN = "\033[32m";
    private static final String CYAN = "\033[36m";
    private static final String NONE = "\033[0m";

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        printer = new ColoredFormatter(console);
    }

    @Test
    public void testPrintMessage() throws Exception {
        Message message = new Message("123", "A", "11111111", "111111", "D", "E", "F", "abc", null);
        printer.printMessage(message);
        verify(console).printOut(colored("$cA$n $gNov 11 11:11:11$n D E $yF$n abc"));
    }

    private String colored(String s) {
        return s.replace("$c", CYAN).replace("$g", GREEN).replace("$y", YELLOW).replace("$n", NONE);
    }

    @Test
    public void testAggregated() throws Exception {
        AggregatedItem item1 = AggregatedItemHelper.map("abc", 123, "abd", 42);
        AggregatedItem item2 = AggregatedItemHelper.map("abc", 124, "abd", "qwe");
        Aggregated aggr = new Aggregated(item1, item2);

        printer.printAggregated(aggr);
        verify(console).printOut(colored("abc:$g123$n abd:$g42$n"));
        verify(console).printOut(colored("abc:$g124$n abd:$gqwe$n"));
    }

    @Test
    public void testPrintStatus() throws Exception {
        printer.printStatus("ABC", 1, "2", 3);
        verify(console).println("ABC", 1, "2", 3);
    }
}
