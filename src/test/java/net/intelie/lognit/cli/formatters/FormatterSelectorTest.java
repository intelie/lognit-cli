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

    @Before
    public void setUp() throws Exception {
        plain = mock(PlainFormatter.class);
        colored = mock(ColoredFormatter.class);
        console = mock(UserConsole.class);
        selector = new FormatterSelector(console, colored, plain);
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

    @Test(expected = IllegalArgumentException.class)
    public void whenIsNullThrowsException() {
        selector.select(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenIsInvalidThrowsException() {
        selector.select("invalid");
    }

}
