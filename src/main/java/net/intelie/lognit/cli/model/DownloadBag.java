package net.intelie.lognit.cli.model;

import java.util.List;

public class DownloadBag {
    private final List<Message> items;
    private final int remaining_docs;

    public DownloadBag(List<Message> items, int remaining_docs) {
        this.items = items;
        this.remaining_docs = remaining_docs;
    }

    public List<Message> getItems() {
        return items;
    }

    public int getRemainingDocs() {
        return remaining_docs;
    }
}
