package net.intelie.lognit.cli.model;

import java.util.List;

public class DownloadBag {
    private final List<Message> items;
    private final Aggregated aggregated;
    private final long total_hits;
    private final long current_hit;

    public DownloadBag(List<Message> items, Aggregated aggregated, long current_hit, long total_hits) {
        this.items = items;
        this.aggregated = aggregated;
        this.total_hits = total_hits;
        this.current_hit = current_hit;
    }

    public List<Message> getItems() {
        return items;
    }

    public Aggregated getAggregated() {
        return aggregated;
    }

    public long getTotalHits() {
        return total_hits;
    }

    public long getCurrentHit() {
        return current_hit;
    }
}
