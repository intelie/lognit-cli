package net.intelie.lognit.cli.model;

import java.io.Serializable;
import java.util.Collection;

public class Stats implements Serializable {
    private final String node;
    private final long total_messages;
    private final long total_bytes;
    private final long total_docs;
    private final Collection<String> queries;
    private final Collection<Long> message_rate;
    private final Collection<Long> byte_rate;

    public Stats(String node, long totalMessages, long totalBytes, long totalDocs, Collection<String> queries, Collection<Long> message_rate, Collection<Long> byte_rate) {
        this.node = node;
        this.total_messages = totalMessages;
        this.total_bytes = totalBytes;
        this.total_docs = totalDocs;
        this.queries = queries;
        this.message_rate = message_rate;
        this.byte_rate = byte_rate;
    }

    public String getNode() {
        return node;
    }

    public long getTotalMessages() {
        return total_messages;
    }

    public long getTotalBytes() {
        return total_bytes;
    }

    public long getTotalDocs() {
        return total_docs;
    }

    public Collection<String> getQueries() {
        return queries;
    }

    public Collection<Long> getDocsRate() {
        return message_rate;
    }

    public Collection<Long> getBytesRate() {
        return byte_rate;
    }
}
