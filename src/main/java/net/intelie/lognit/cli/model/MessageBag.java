package net.intelie.lognit.cli.model;

import java.util.List;

public class MessageBag {
    private final List<Message> items;
    private final Aggregated aggregated;
    private final String message;
    private final boolean success;
    private final boolean realtime;
    private final Long total_items;
    private final Integer total_nodes;
    private final SearchStats stats;
    private final String node;
    private final Long time;

    public MessageBag(List<Message> items, SearchStats stats, Aggregated aggregated, String node, Long time, String message, boolean success, boolean realtime, Integer totalNodes, Long totalItems) {
        this.items = items;
        this.stats = stats;
        this.node = node;
        this.time = time;
        this.message = message;
        this.aggregated = aggregated;
        this.success = success;
        this.realtime = realtime;
        this.total_nodes = totalNodes;
        this.total_items = totalItems;
    }


    public List<Message> getItems() {
        return items;
    }

    public SearchStats getStats() {
        return stats;
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

    public Long getTotalItems() {
        return total_items;
    }

    public Integer getTotalNodes() {
        return total_nodes;
    }

    public String getNode() {
        return node;
    }

    public Long getTime() {
        return time;
    }

    public Aggregated getAggregated() {
        return aggregated;
    }
}
