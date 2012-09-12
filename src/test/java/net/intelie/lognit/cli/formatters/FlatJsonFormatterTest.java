package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.AggregatedItemHelper;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.json.Jsonizer;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.AggregatedItem;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.SearchStats;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class FlatJsonFormatterTest {
    private FlatJsonFormatter printer;
    private UserConsole console;
    private Jsonizer jsonizer;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        jsonizer = mock(Jsonizer.class);
        printer = new FlatJsonFormatter(console, jsonizer);
    }

    @Test
    public void testPrintMessage() throws Exception {
        Message message = new Message("123", "A", "11111111", "111111", "D", "E", "F", "abc", null);
        when(jsonizer.toFlat(message)).thenReturn("abc");
        printer.print(message);
        verify(console).printOut("%s", "abc");
    }

    @Test
    public void testAggregated() throws Exception {
        AggregatedItem item1 = AggregatedItemHelper.map("abc", 123);
        AggregatedItem item2 = AggregatedItemHelper.map("abc", 124);
        Aggregated aggr = new Aggregated(item1, item2);

        when(jsonizer.toFlat(item1)).thenReturn("abc");
        when(jsonizer.toFlat(item2)).thenReturn("qwe");
        printer.print(aggr);
        verify(console).printOut("%s", "abc");
        verify(console).printOut("%s", "qwe");
    }

    @Test
    public void testPrintStats() throws Exception {
        SearchStats stats = mock(SearchStats.class);
        when(jsonizer.to(stats)).thenReturn("abc");
        printer.print(stats);
        verify(console).printOut("%s", "abc");
    }

    @Test
    public void testPrintStatus() throws Exception {
        printer.printStatus("ABC", 1, "2", 3);
        verify(console).println("ABC", 1, "2", 3);
    }
}
