package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.UserConsole;
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

    @Before
    public void setUp() throws Exception {
        plain = mock(PlainFormatter.class);
        colored = mock(ColoredFormatter.class);
        json = mock(JsonFormatter.class);
        flatJson = mock(FlatJsonFormatter.class);
        console = mock(UserConsole.class);
        selector = new FormatterSelector(console, colored, plain, json, flatJson);
    }

    @Test
    public void whenSelectingColoredWithTtyTerminal() {
        when(console.isTTY()).thenReturn(true);
        assertThat(selector.select("colored")).isSameAs(colored);
    }

    @Test
    public void whenSelectingColoredWithNonTtyTerminal() {
        when(console.isTTY()).thenReturn(false);
        assertThat(selector.select("colored")).isSameAs(plain);
    }
    @Test
    public void whenSelectingPlain() {
        when(console.isTTY()).thenReturn(true);
        assertThat(selector.select("plain")).isSameAs(plain);
    }

    @Test
    public void whenSelectingJson() {
        assertThat(selector.select("json")).isSameAs(json);
    }

    @Test
    public void whenSelectingFlatJson() {
        assertThat(selector.select("flat-json")).isSameAs(flatJson);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenIsNullThrowsException() {
        selector.select(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenIsInvalidThrowsException() {
        selector.select("invalid");
    }

}
