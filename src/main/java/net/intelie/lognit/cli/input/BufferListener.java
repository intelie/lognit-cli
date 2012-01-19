package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.http.RestListener;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.MessageBag;

public class BufferListener implements RestListener<MessageBag> {
    @Override
    public void receive(MessageBag messages) {
        for (Message message : messages.getItems())
            System.out.println(message.getMessage());
    }
}
