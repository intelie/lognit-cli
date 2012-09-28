package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.AggregatedItemHelper;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.AggregatedItem;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.SearchStats;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class PlainFormatterTest {
    private PlainFormatter printer;
    private UserConsole console;
    private BarsFormatter bars;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        bars = mock(BarsFormatter.class);
        printer = new PlainFormatter(console, bars);
    }

    @Test
    public void hasConsoleOnlyConstructor() throws Exception {
        new PlainFormatter(mock(UserConsole.class));
    }


    @Test
    public void testPrintMessageNoTty() throws Exception {
        when(console.isTTY()).thenReturn(false);
        Message message = new Message("123", "A", "11111111", "111111", "D", "E", "F", "abc", null);
        printer.print(message);
        verify(console).printOut("A Nov 11 11:11:11 D E F abc");
    }

    @Test
    public void testAggregatedNoTty() throws Exception {
        when(console.isTTY()).thenReturn(false);
        AggregatedItem item1 = AggregatedItemHelper.map("abc", 123, "abd", 42);
        AggregatedItem item2 = AggregatedItemHelper.map("abc", 124, "abd", "qwe");
        Aggregated aggr = new Aggregated(item1, item2);

        printer.print(aggr);
        verify(console).printOut("abc:123 abd:42");
        verify(console).printOut("abc:124 abd:qwe");
    }
    @Test
    public void testPrintStatus() throws Exception {
        printer.printStatus("ABC", 1, "2", 3);
        verify(console).println("ABC", 1, "2", 3);
    }

    @Test
    public void testPrintStats() throws Exception {
        when(console.isTTY()).thenReturn(true);

        List hours = mock(List.class);
        List last = mock(List.class);
        List hosts = mock(List.class);
        List others = mock(List.class);
        Map fields = new LinkedHashMap();
        fields.put("host", hosts);
        fields.put("other", others);

        SearchStats stats = new SearchStats(hours, last, fields);

        when(bars.hours(hours, false)).thenReturn(Arrays.asList("aaa", "aaa", "aaa"));
        when(bars.lastHour(last, false)).thenReturn(Arrays.asList("bbb"));
        when(bars.field("host", hosts, false)).thenReturn(Arrays.asList("ccc"));
        when(bars.field("other", others, false)).thenReturn(Arrays.asList("ddd", "ddd"));

        printer.print(stats);

        verify(console, times(0)).isTTY();

        InOrder orderly = inOrder(console);
        orderly.verify(console).printOut("%s%s", "aaa", "bbb");
        orderly.verify(console).printOut("%s%s", "aaa", "");
        orderly.verify(console).printOut("%s%s", "aaa", "ccc");
        orderly.verify(console).printOut("%s%s", "", "");
        orderly.verify(console, times(2)).printOut("%s%s", "ddd", "");
        verifyNoMoreInteractions(console);
    }
}
