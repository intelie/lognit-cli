package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.input.UserConsole;
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

    @Before
    public void setUp() throws Exception {
        plain = mock(PlainFormatter.class);
        colored = mock(ColoredFormatter.class);
        json = mock(JsonFormatter.class);
        console = mock(UserConsole.class);
        selector = new FormatterSelector(console, colored, plain, json);
    }

    @Test
    public void whenSelectingColoredWithTtyTerminal() {
        when(console.isTTY()).thenReturn(true);
        Formatter selected = selector.select("colored");
        assertThat(selected).isSameAs(colored);
    }

    @Test
    public void whenSelectingColoredWithNonTtyTerminal() {
        when(console.isTTY()).thenReturn(false);
        Formatter selected = selector.select("colored");
        assertThat(selected).isSameAs(plain);
    }
    @Test
    public void whenSelectingPlain() {
        when(console.isTTY()).thenReturn(true);
        Formatter selected = selector.select("plain");
        assertThat(selected).isSameAs(plain);
    }

    @Test
    public void whenSelectingJson() {
        Formatter selected = selector.select("json");
        assertThat(selected).isSameAs(json);
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
