package net.intelie.lognit.cli.http;

import org.cometd.client.BayeuxClient;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BayeuxHandleTest {
    @Test
    public void willDisconnectWhenClosing() {
        BayeuxClient client = mock(BayeuxClient.class);
        BayeuxHandle handle = new BayeuxHandle(client);
        handle.close();
        verify(client).disconnect();
    }
}
