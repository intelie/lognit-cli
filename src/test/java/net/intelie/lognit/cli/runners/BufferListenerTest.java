package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.formatters.ColoredFormatter;
import net.intelie.lognit.cli.http.RestListener;
import net.intelie.lognit.cli.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static net.intelie.lognit.cli.AggregatedItemHelper.map;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BufferListenerTest {

    private ColoredFormatter printer;
    private BufferListener listener;

    @Before
    public void setUp() throws Exception {
        printer = mock(ColoredFormatter.class);
        listener = new BufferListener(printer, false);
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
        verify(printer).printStatus(eq(BufferListener.RESPONSE_RECEIVED), anyVararg());


        assertThat(listener.waitHistoric(50, 3)).isFalse();

        verify(printer).printStatus(BufferListener.MISSING_NODES_RESPONSE);
        verify(printer).print(mA);
        verify(printer).print(mB);
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void canReceiveAsRestListener() {
        RestListener restListener = (RestListener) listener;
        Message mA = m("A"), mB = m("B");
        restListener.receive(ms(false, true, 2, mB, mA));
        verify(printer).printStatus(eq(BufferListener.RESPONSE_RECEIVED), anyVararg());
        assertThat(listener.waitHistoric(50, 3)).isFalse();

        verify(printer).printStatus(BufferListener.MISSING_NODES_RESPONSE);
        verify(printer).print(mA);
        verify(printer).print(mB);
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
        verify(printer).printStatus(eq(BufferListener.RESPONSE_RECEIVED), anyVararg());
        listener.receive(ms(true, true, 2, mC));
        listener.receive(ms("ABC"));
        verify(printer).printStatus(BufferListener.QUERY_CANCELLED, "ABC");

        assertThat(listener.waitHistoric(50, 3)).isFalse();

        verify(printer).printStatus(BufferListener.MISSING_NODES_RESPONSE);
        verify(printer).print(mA);
        verify(printer).print(mB);
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void afterReleasePrintsAllOthers() {
        Message mA = m("A"), mB = m("B"), mC = m("C");
        listener.receive(ms(true, true, 2, mA, mC));
        listener.receive(ms(true, true, 2, mB));
        listener.releaseAll();

        verify(printer).print(mC);
        verify(printer).print(mA);
        verify(printer).print(mB);
        verifyNoMoreInteractions(printer);
        listener.releaseAll();
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void willShowWarningWhenQueryFails() {
        Message mA = m("A"), mB = m("B"), mC = m("C");
        listener.receive(ms(false, false, 2, mB));
        listener.releaseAll();

        verify(printer).printStatus(BufferListener.QUERY_CANCELLED, (Object) null);
        verifyNoMoreInteractions(printer);
        listener.releaseAll();
        verifyNoMoreInteractions(printer);
    }


    @Test(timeout = 1000)
    public void willNotHoldAnymoreAfterFirstReleaseAll() {
        Message mA = m("A"), mB = m("B"), mC = m("C");
        listener.releaseAll();
        listener.receive(ms(false, true, 2, mA, mC));
        verify(printer).printStatus(eq(BufferListener.RESPONSE_RECEIVED), anyVararg());
        listener.receive(ms(true, true, 2, mB));

        verify(printer).print(mC);
        verify(printer).print(mA);
        verify(printer).print(mB);
        verifyNoMoreInteractions(printer);
        listener.releaseAll();
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void willNotHoldAnymoreAfterFirstReleaseAllPrintingAggregation() {
        Aggregated aggregated = new Aggregated(
                map("abc", 123), map("qwe", 234));

        Message mA = m("A"), mB = m("B"), mC = m("C");
        listener.releaseAll();
        listener.receive(ms(true, true, "node", 2L, aggregated));

        verify(printer).print(aggregated);
        verifyNoMoreInteractions(printer);
        listener.releaseAll();
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void willPrintRealTimeInReceivingOrder() {
        Message mA = m("A"), mB = m("B"), mC = m("C");
        listener.releaseAll();
        listener.receive(ms(true, true, "node", 2L, 2, m("A"), m("B")));

        InOrder orderly = inOrder(printer);
        orderly.verify(printer).print(m("A"));
        orderly.verify(printer).print(m("B"));
        orderly.verifyNoMoreInteractions();
    }


    @Test(timeout = 1000)
    public void whenOneOfOneMessagesAriveOnTime() {
        Message mA = m("A"), mB = m("B");
        listener.receive(ms(false, true, 1, mB, mA));
        verify(printer).printStatus(eq(BufferListener.RESPONSE_RECEIVED), anyVararg());
        assertThat(listener.waitHistoric(10000, 3)).isTrue();

        verify(printer).print(mA);
        verify(printer).print(mB);
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void wontCauseExceptionIfOneMessageArrivesWithoutClusterInfo() {
        Message mA = m("A"), mB = m("B");
        listener.receive(ms(false, true, 0, mB, mA));
        verify(printer).printStatus(eq(BufferListener.RESPONSE_RECEIVED), anyVararg());

        long start = System.currentTimeMillis();
        assertThat(listener.waitHistoric(50, 3)).isFalse();

        verify(printer).printStatus(BufferListener.NO_CLUSTER_INFO);
        verify(printer).print(mA);
        verify(printer).print(mB);
        verifyNoMoreInteractions(printer);
    }


    @Test(timeout = 1000)
    public void whenTwoOfTwoMessagesArriveOntime() {
        Message mA = m("A"), mB = m("B"), mC = m("C"), mD = m("D");
        listener.receive(ms(false, true, 2, mB, mA));
        listener.receive(ms(false, true, 2, mC, mD));
        verify(printer, times(2)).printStatus(eq(BufferListener.RESPONSE_RECEIVED), anyVararg());

        assertThat(listener.waitHistoric(10000, 3)).isTrue();

        verify(printer).print(mB);
        verify(printer).print(mC);
        verify(printer).print(mD);
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void whenTwoOfTwoMessagesArriveOntimePrintStats() {
        listener = new BufferListener(printer, true);

        SearchStats A = new SearchStats(
                Arrays.asList(new FreqPoint<Long>(1L, 2)),
                Arrays.asList(new FreqPoint<Long>(3L, 4)),
                new HashMap<String, List<FreqPoint<String>>>() {{
                    put("host", Arrays.asList(new FreqPoint<String>("A", 5)));
                }});

        SearchStats B = new SearchStats(
                Arrays.asList(new FreqPoint<Long>(1L, 2)),
                Arrays.asList(new FreqPoint<Long>(4L, 5)),
                new HashMap<String, List<FreqPoint<String>>>() {{
                    put("host", Arrays.asList(new FreqPoint<String>("A", 2), new FreqPoint<String>("B", 3)));
                    put("what", Arrays.asList(new FreqPoint<String>("C", 2)));
                }});

        SearchStats merged = new SearchStats();
        merged.merge(A);
        merged.merge(B);

        Message mA = m("A"), mB = m("B"), mC = m("C"), mD = m("D");
        listener.receive(new MessageBag(Arrays.asList(mB, mA), A, null, "AAA", 3L, null, true, false, 2, 42L));
        verify(printer).printStatus(BufferListener.RESPONSE_RECEIVED, "AAA", 1, 2, 2, 42L, 3L);
        listener.receive(new MessageBag(Arrays.asList(mC, mD), B, null, "BBB", 5L, null, true, false, 3, 42L));
        verify(printer).printStatus(BufferListener.RESPONSE_RECEIVED, "BBB", 2, 3, 2, 42L, 5L);

        assertThat(listener.waitHistoric(10000, 3)).isTrue();

        verify(printer).print(merged);
        verifyNoMoreInteractions(printer);
    }

    @Test(timeout = 1000)
    public void willPrintEvenIfReceivesAfterTimeout() {
        listener = new BufferListener(printer, false);

        Message mA = m("A"), mB = m("B"), mC = m("C"), mD = m("D");
        listener.receive(ms(false, true, "AAA", 3L, 2, mB, mA));
        verify(printer).printStatus(BufferListener.RESPONSE_RECEIVED, "AAA", 1, 2, 2, 42L, 3L);

        assertThat(listener.waitHistoric(50, 3)).isFalse();
        verify(printer).printStatus(BufferListener.MISSING_NODES_RESPONSE);

        verify(printer).print(mB);
        verify(printer).print(mA);

        listener.receive(ms(false, true, "BBB", 5L, 3, mC, mD));
        verify(printer).printStatus(BufferListener.RESPONSE_RECEIVED, "BBB", 2, 3, 2, 42L, 5L);

        verifyNoMoreInteractions(printer);
    }

    private MessageBag ms(String message) {
        return new MessageBag(null, null, null, null, null, message, false, false, 0, 0L);
    }

    private MessageBag ms(boolean realtime, boolean success, int nodes, Message... messages) {
        return ms(realtime, success, null, 0L, nodes, messages);
    }

    private MessageBag ms(boolean realtime, boolean success, String node, Long time, int nodes, Message... messages) {
        return new MessageBag(Arrays.asList(messages), null, null, node, time, null, success, realtime, nodes, 42L);
    }

    private MessageBag ms(boolean realtime, boolean success, String node, Long time, Aggregated aggregated) {
        return new MessageBag(null, null, aggregated, node, time, null, success, realtime, 0, 0L);
    }


    private Message m(String id) {
        return new Message(id);
    }

}
