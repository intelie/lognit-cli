package net.intelie.lognit.cli.http;

import net.intelie.lognit.cli.model.Welcome;
import org.cometd.bayeux.Message;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JsonMessageListenerTest {
    @Test
    public void testOnMessage() throws Exception {
        RestListener<Welcome> restListener = mock(RestListener.class);
        Welcome welcome = new Welcome("abcabcabc");

        Jsonizer jsonizer = mock(Jsonizer.class);
        when(jsonizer.from("blablabla", Welcome.class)).thenReturn(welcome);

        Message message = mock(Message.class);
        when(message.getData()).thenReturn("blablabla");

        JsonMessageListener<Welcome> listener = new JsonMessageListener<Welcome>(restListener, Welcome.class, jsonizer);
        listener.onMessage(null, message);
        verify(restListener).receive(welcome);
    }
}
