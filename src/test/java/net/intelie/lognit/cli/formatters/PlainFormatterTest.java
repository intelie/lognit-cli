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
import static org.mockito.Mockito.when;

public class PlainFormatterTest {
    private PlainFormatter printer;
    private UserConsole console;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        printer = new PlainFormatter(console);
    }

    @Test
    public void testPrintMessageNoTty() throws Exception {
        when(console.isTTY()).thenReturn(false);
        Message message = new Message("123", "A", "11111111", "111111", "D", "E", "F", "abc", null);
        printer.printMessage(message);
        verify(console).printOut("A Nov 11 11:11:11 D E F abc");
    }

    @Test
    public void testAggregatedNoTty() throws Exception {
        when(console.isTTY()).thenReturn(false);
        AggregatedItem item1 = AggregatedItemHelper.map("abc", 123, "abd", 42);
        AggregatedItem item2 = AggregatedItemHelper.map("abc", 124, "abd", "qwe");
        Aggregated aggr = new Aggregated(item1, item2);

        printer.printAggregated(aggr);
        verify(console).printOut("abc:123 abd:42");
        verify(console).printOut("abc:124 abd:qwe");
    }
    @Test
    public void testPrintStatus() throws Exception {
        printer.printStatus("ABC", 1, "2", 3);
        verify(console).println("ABC", 1, "2", 3);
    }
}
