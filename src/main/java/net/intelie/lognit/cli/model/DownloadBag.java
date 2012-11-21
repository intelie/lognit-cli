package net.intelie.lognit.cli.model;

import java.util.List;

public class DownloadBag {
    private final List<Message> items;
    private final long total_hits;
    private final long current_hit;

    public DownloadBag(List<Message> items, long current_hit, long total_hits) {
        this.items = items;
        this.total_hits = total_hits;
        this.current_hit = current_hit;
    }

    public List<Message> getItems() {
        return items;
    }

    public long getTotalHits() {
        return total_hits;
    }

    public long getCurrentHit() {
        return current_hit;
    }
}
