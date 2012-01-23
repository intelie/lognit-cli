package net.intelie.lognit.cli.model;

import java.util.List;

public class MessageBag {
    private final List<Message> items;
    private final String message;
    private final boolean success;
    private final boolean realtime;
    private final int total_items;
    private final int total_nodes;

    public MessageBag(List<Message> items, String message, boolean success, boolean realtime, int totalNodes, int totalItems) {
        this.items = items;
        this.message = message;
        this.success = success;
        this.realtime = realtime;
        this.total_nodes = totalNodes;
        this.total_items = totalItems;
    }

    public List<Message> getItems() {
        return items;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isRealtime() {
        return realtime;
    }

    public boolean isHistoric() {
        return !realtime && success;
    }

    public int getTotalItems() {
        return total_items;
    }

    public int getTotalNodes() {
        return total_nodes;
    }
}
