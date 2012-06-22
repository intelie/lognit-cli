package net.intelie.lognit.cli.http;

import org.cometd.client.BayeuxClient;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BayeuxHandleTest {
    @Test
    public void willDisconnectWhenClosing() {
        BayeuxClient client = mock(BayeuxClient.class);
        BayeuxHandle handle = new BayeuxHandle(client);
        handle.close();
        verify(client).disconnect();
    }

    @Test
    public void waitDisconnectedWaitsForSoLong() {
        BayeuxClient client = mock(BayeuxClient.class);
        BayeuxHandle handle = new BayeuxHandle(client);
        handle.waitDisconnected();
        verify(client).waitFor(Long.MAX_VALUE, BayeuxClient.State.UNCONNECTED);
    }


    @Test
    public void onlyClosesOnce() {
        BayeuxClient client = mock(BayeuxClient.class);
        BayeuxHandle handle = new BayeuxHandle(client);
        handle.close();
        verify(client).disconnect();
        handle.close();
        verifyNoMoreInteractions(client);
    }

}
