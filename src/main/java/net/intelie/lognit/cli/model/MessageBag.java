package net.intelie.lognit.cli.model;

import java.util.List;

public class MessageBag {
    private final List<Message> items;
    private final String message;
    private final boolean success;
    private final boolean realtime;
    private final int total_items;
    private final int total_nodes;
    private final String node;
    private final Long time;
    
    public MessageBag(List<Message> items, String node, Long time, String message, boolean success, boolean realtime, int totalNodes, int totalItems) {
        this.items = items;
        this.node = node;
        this.time = time;
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

    public String getNode() {
        return node;
    }

    public Long getTime() {
        return time;
    }
}
