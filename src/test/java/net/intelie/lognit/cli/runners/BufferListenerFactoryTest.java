package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.formatters.Formatter;
import net.intelie.lognit.cli.formatters.FormatterSelector;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BufferListenerFactoryTest {

    private BufferListenerFactory listenerFactory;
    private FormatterSelector selector;

    @Before
    public void setUp() throws Exception {
        selector = mock(FormatterSelector.class);
        listenerFactory = new BufferListenerFactory(selector);
    }

    @Test
    public void whenCreating() throws Exception {
        Formatter formatter = mock(Formatter.class);
        when(selector.select("test")).thenReturn(formatter);
        BufferListener listener = listenerFactory.create("test", false);
        assertThat(listener.getFormatter()).isEqualTo(formatter);
        assertThat(listener.isVerbose()).isEqualTo(false);
    }

}
