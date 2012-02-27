package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.formatters.ColoredFormatter;
import net.intelie.lognit.cli.formatters.PlainFormatter;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BufferListenerFactoryTest {

    private PlainFormatter defaultPrinter;
    private ColoredFormatter coloredPrinter;
    private UserConsole userConsole;
    private BufferListenerFactory listenerFactory;

    @Before
    public void setUp() throws Exception {
        defaultPrinter = mock(PlainFormatter.class);
        coloredPrinter = mock(ColoredFormatter.class);
        userConsole = mock(UserConsole.class);
        listenerFactory = new BufferListenerFactory(userConsole, coloredPrinter, defaultPrinter);
    }

    @Test
    public void creatingForcingNoColor() {
        when(userConsole.isTTY()).thenReturn(true);
        BufferListener listener = listenerFactory.create(true, false);
        assertThat(listener.getPrinter()).isEqualTo(defaultPrinter);
        assertThat(listener.isVerbose()).isEqualTo(false);
    }

    @Test
    public void creatingVerboseForcingNoColor() {
        when(userConsole.isTTY()).thenReturn(true);
        BufferListener listener = listenerFactory.create(true, true);
        assertThat(listener.getPrinter()).isEqualTo(defaultPrinter);
        assertThat(listener.isVerbose()).isEqualTo(true);
    }


    @Test
    public void createOnTTY() {
        when(userConsole.isTTY()).thenReturn(true);
        BufferListener listener = listenerFactory.create(false, false);
        assertThat(listener.getPrinter()).isEqualTo(coloredPrinter);
        assertThat(listener.isVerbose()).isEqualTo(false);
    }

    @Test
    public void createOnNonTTY() {
        when(userConsole.isTTY()).thenReturn(false);
        BufferListener listener = listenerFactory.create(false, false);
        assertThat(listener.getPrinter()).isEqualTo(defaultPrinter);
        assertThat(listener.isVerbose()).isEqualTo(false);
    }
}
