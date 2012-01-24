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
        verify(printer).printStatus(BufferListener.MISSING_NODES_RESPONSE);
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void whenOnlyOneOfTwoMessagesAriveOnTime() {
        Message mA = m("A"), mB = m("B");
        listener.receive(ms(false, true, 2, mB, mA));
        assertThat(listener.waitHistoric(50, 3)).isFalse();

        verify(printer).printStatus(BufferListener.MISSING_NODES_RESPONSE);
        verify(printer).printMessage(mA);
        verify(printer).printMessage(mB);
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void whenOnlyOneOfTwoMessagesAriveOnTimeAndTheThreadInterrupts() throws Exception {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                assertThat(listener.waitHistoric(10000, 3)).isFalse();
            }
        });
        t.start();
        t.interrupt();
        t.join();

        verify(printer).printStatus(BufferListener.MISSING_NODES_RESPONSE);
        verifyNoMoreInteractions(printer);
    }


    @Test(timeout = 1000)
    public void whenOnlyOneOfTwoMessagesAriveOnTimeRealTimeAndFailedDoesntCount() {
        Message mA = m("A"), mB = m("B"), mC = m("C");
        listener.receive(ms(false, true, 2, mB, mA));
        listener.receive(ms(true, true, 2, mC));
        listener.receive(ms(false, false, 2, mC));
        assertThat(listener.waitHistoric(50, 3)).isFalse();


        verify(printer).printStatus(BufferListener.MISSING_NODES_RESPONSE);
        verify(printer).printMessage(mA);
        verify(printer).printMessage(mB);
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void afterReleasePrintsAllOthers() {
        Message mA = m("A"), mB = m("B"), mC = m("C");
        listener.receive(ms(true, true, 2, mA, mC));
        listener.receive(ms(true, true, 2, mB));
        listener.releaseAll();

        verify(printer).printMessage(mC);
        verify(printer).printMessage(mA);
        verify(printer).printMessage(mB);
        verifyNoMoreInteractions(printer);
        listener.releaseAll();
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void willShowWarningWhenQueryFails() {
        Message mA = m("A"), mB = m("B"), mC = m("C");
        listener.receive(ms(false, false, 2, mB));
        listener.releaseAll();

        verify(printer).printStatus(BufferListener.QUERY_CANCELLED, null);
        verifyNoMoreInteractions(printer);
        listener.releaseAll();
        verifyNoMoreInteractions(printer);
    }


    @Test(timeout = 1000)
    public void willNotHoldAnymoreAfterFirstReleaseAll() {
        Message mA = m("A"), mB = m("B"), mC = m("C");
        listener.releaseAll();
        listener.receive(ms(false, true, 2, mA, mC));
        listener.receive(ms(true, true, 2, mB));

        verify(printer).printMessage(mC);
        verify(printer).printMessage(mA);
        verify(printer).printMessage(mB);
        verifyNoMoreInteractions(printer);
        listener.releaseAll();
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

        long start = System.currentTimeMillis();
        assertThat(listener.waitHistoric(50, 3)).isFalse();
        assertThat(System.currentTimeMillis() - start).isGreaterThanOrEqualTo(50);

        verify(printer).printStatus(BufferListener.NO_CLUSTER_INFO);
        verify(printer).printMessage(mA);
        verify(printer).printMessage(mB);
        verifyNoMoreInteractions(printer);
    }


    @Test(timeout = 1000)
    public void whenTwoOfTwoMessagesArriveOntime() {
        Message mA = m("A"), mB = m("B"), mC = m("C"), mD = m("D");
        listener.receive(ms(false, true, 2, mB, mA));
        listener.receive(ms(false, true, 2, mC, mD));
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
        return new Message(id);
    }

}
