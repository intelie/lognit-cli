package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.formatters.iem.IEMSender;
import net.intelie.lognit.cli.formatters.iem.IEMSenderFactory;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormatterSelectorTest {

    private PlainFormatter plain;
    private ColoredFormatter colored;
    private UserConsole console;
    private FormatterSelector selector;
    private JsonFormatter json;
    private FlatJsonFormatter flatJson;
    private IEMSenderFactory iemFactory;

    @Before
    public void setUp() throws Exception {
        plain = mock(PlainFormatter.class);
        colored = mock(ColoredFormatter.class);
        json = mock(JsonFormatter.class);
        flatJson = mock(FlatJsonFormatter.class);
        console = mock(UserConsole.class);
        iemFactory = mock(IEMSenderFactory.class);
        selector = new FormatterSelector(console, colored, plain, json, flatJson, iemFactory);
    }

    @Test
    public void whenSelectingColoredWithTtyTerminal() throws Exception {
        when(console.isTTY()).thenReturn(true);
        assertThat(selector.select("colored")).isSameAs(colored);
    }

    @Test
    public void whenSelectingColoredWithNonTtyTerminal() throws Exception {
        when(console.isTTY()).thenReturn(false);
        assertThat(selector.select("colored")).isSameAs(plain);
    }

    @Test
    public void whenSelectingPlain() throws Exception {
        when(console.isTTY()).thenReturn(true);
        assertThat(selector.select("plain")).isSameAs(plain);
    }

    @Test
    public void whenSelectingJson() throws Exception {
        assertThat(selector.select("json")).isSameAs(json);
    }

    @Test
    public void whenSelectingFlatJson() throws Exception {
        assertThat(selector.select("flat-json")).isSameAs(flatJson);
    }

    @Test
    public void whenSelectingIEMSender() throws Exception {
        IEMSender sender = mock(IEMSender.class);
        when(iemFactory.create("iem://abc")).thenReturn(sender);

        assertThat(selector.select("iem://abc")).isSameAs(sender);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenIsNullThrowsException() throws Exception {
        selector.select(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenIsInvalidThrowsException() throws Exception {
        selector.select("invalid");
    }

}
