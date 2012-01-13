package net.intelie.lognit.cli.model;

import java.util.List;

public class MessageBag {
    private final List<Message> items;
    private final String message;

    public MessageBag(List<Message> items, String message) {
        this.items = items;
        this.message = message;
    }
}
