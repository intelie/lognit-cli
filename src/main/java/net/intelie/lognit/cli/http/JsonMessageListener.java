package net.intelie.lognit.cli.http;

import com.google.inject.Inject;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;

class JsonMessageListener<T> implements ClientSessionChannel.MessageListener {
    private final RestListener<T> listener;
    private final Class<T> type;
    private final Jsonizer jsonizer;

    @Inject
    public JsonMessageListener(RestListener<T> listener, Class<T> type, Jsonizer jsonizer) {
        this.listener = listener;
        this.type = type;
        this.jsonizer = jsonizer;
    }

    @Override
    public void onMessage(ClientSessionChannel clientSessionChannel, Message message) {
        listener.receive(jsonizer.from((String) message.getData(), type));
    }
}
