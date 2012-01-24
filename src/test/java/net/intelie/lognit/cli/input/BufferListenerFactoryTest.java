package net.intelie.lognit.cli.input;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BufferListenerFactoryTest {

    private DefaultMessagePrinter defaultPrinter;
    private ColoredMessagePrinter coloredPrinter;
    private UserConsole userConsole;
    private BufferListenerFactory listenerFactory;

    @Before
    public void setUp() throws Exception {
        defaultPrinter = mock(DefaultMessagePrinter.class);
        coloredPrinter = mock(ColoredMessagePrinter.class);
        userConsole = mock(UserConsole.class);
        listenerFactory = new BufferListenerFactory(userConsole, coloredPrinter, defaultPrinter);
    }

    @Test
    public void creatingForcingNoColor() {
        when(userConsole.isTTY()).thenReturn(true);
        BufferListener listener = listenerFactory.create(true);
        assertThat(listener.getPrinter()).isEqualTo(defaultPrinter);
    }

    @Test
    public void createOnTTY() {
        when(userConsole.isTTY()).thenReturn(true);
        BufferListener listener = listenerFactory.create(false);
        assertThat(listener.getPrinter()).isEqualTo(coloredPrinter);
    }

    @Test
    public void createOnNonTTY() {
        when(userConsole.isTTY()).thenReturn(false);
        BufferListener listener = listenerFactory.create(false);
        assertThat(listener.getPrinter()).isEqualTo(defaultPrinter);
    }
}
