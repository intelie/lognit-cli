package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.MessageBag;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BufferListenerTest {

    private MessagePrinter printer;
    private BufferListener listener;

    @Before
    public void setUp() throws Exception {
        printer = mock(MessagePrinter.class);
        listener = new BufferListener(printer);
    }

    @Test(timeout = 1000)
    public void whenNoMessageArriveOnTime() {
        assertThat(listener.waitHistoric(50, 3)).isFalse();

        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void whenOnlyOneOfTwoMessagesAriveOnTime() {
        Message mA = m("A"), mB = m("B");
        listener.receive(ms(false, true, 2, mB, mA));
        assertThat(listener.waitHistoric(50, 3)).isFalse();

        verify(printer).printMessage(mA);
        verify(printer).printMessage(mB);
        verifyNoMoreInteractions(printer);
    }


    @Test(timeout = 1000)
    public void whenOnlyOneOfTwoMessagesAriveOnTimeRealTimeAndFailedDoesntCount() {
        Message mA = m("A"), mB = m("B"), mC=m("C");
        listener.receive(ms(false, true, 2, mB, mA));
        listener.receive(ms(true, true, 2, mC));
        listener.receive(ms(false, false, 2, mC));
        assertThat(listener.waitHistoric(50, 3)).isFalse();

        verify(printer).printMessage(mA);
        verify(printer).printMessage(mB);
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void whenOneOfOneMessagesAriveOnTime() {
        Message mA = m("A"), mB = m("B");
        listener.receive(ms(false, true, 1, mB, mA));
        assertThat(listener.waitHistoric(10000, 3)).isTrue();

        verify(printer).printMessage(mA);
        verify(printer).printMessage(mB);
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void wontCauseExceptionIfOneMessageArrivesWithoutClusterInfo() {
        Message mA = m("A"), mB = m("B");
        listener.receive(ms(false, true, 0, mB, mA));
        assertThat(listener.waitHistoric(10000, 3)).isTrue();

        verify(printer).printMessage(mA);
        verify(printer).printMessage(mB);
        verifyNoMoreInteractions(printer);
    }


    @Test(timeout = 1000)
    public void whenTwoOfTwoMessagesArriveOntime() {
        Message mA = m("A"), mB = m("B"), mC = m("C"), mD = m("D");
        listener.receive(ms(false, true, 1, mB, mA));
        listener.receive(ms(false, true, 1, mC, mD));
        assertThat(listener.waitHistoric(10000, 3)).isTrue();

        verify(printer).printMessage(mB);
        verify(printer).printMessage(mC);
        verify(printer).printMessage(mD);
        verifyNoMoreInteractions(printer);
    }

    private MessageBag ms(boolean realtime, boolean success, int nodes, Message... messages) {
        return new MessageBag(Arrays.asList(messages), null, success, realtime, nodes, 0);
    }

    private Message m(String id) {
        return new Message(id, null);
    }

}
