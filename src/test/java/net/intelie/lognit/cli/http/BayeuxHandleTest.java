package net.intelie.lognit.cli.http;

import org.cometd.client.BayeuxClient;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BayeuxHandleTest {
    @Test
    public void willDisconnectWhenClosing() {
        BayeuxClient client = mock(BayeuxClient.class);
        BayeuxHandle handle = new BayeuxHandle(client, channel);
        handle.close();
        verify(client).disconnect();
    }

    @Test
    public void waitDisconnectedWaitsForSoLong() {
        BayeuxClient client = mock(BayeuxClient.class);
        BayeuxHandle handle = new BayeuxHandle(client, channel);
        when(client.waitFor(1000, BayeuxClient.State.UNCONNECTED, BayeuxClient.State.DISCONNECTED)).thenReturn(true);
        handle.waitDisconnected();
        verify(client).waitFor(1000, BayeuxClient.State.UNCONNECTED, BayeuxClient.State.DISCONNECTED);
    }


    @Test
    public void onlyClosesOnce() {
        BayeuxClient client = mock(BayeuxClient.class);
        BayeuxHandle handle = new BayeuxHandle(client, channel);
        handle.close();
        verify(client).disconnect();
        handle.close();
        verifyNoMoreInteractions(client);
    }

}
