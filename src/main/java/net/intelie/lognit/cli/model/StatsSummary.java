package net.intelie.lognit.cli.model;

import java.util.Collection;

public class StatsSummary {
    private final long total_messages;
    private final long total_bytes;
    private final long total_docs;
    private final Collection<String> nodes;
    private final Collection<String> queries;
    private final Collection<Stats> per_nodes;
    private final Collection<Long> message_rate;
    private final Collection<Long> byte_rate;
    private int missing;

    public StatsSummary(long total_messages, long total_bytes, long total_docs, Collection<String> nodes, Collection<String> queries, Collection<Stats> per_nodes, Collection<Long> message_rate, Collection<Long> byte_rate, int missing) {
        this.total_messages = total_messages;
        this.total_bytes = total_bytes;
        this.total_docs = total_docs;
        this.nodes = nodes;
        this.queries = queries;
        this.per_nodes = per_nodes;
        this.message_rate = message_rate;
        this.byte_rate = byte_rate;
        this.missing = missing;
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

    public Collection<String> getNodes() {
        return nodes;
    }

    public Collection<String> getQueries() {
        return queries;
    }

    public Collection<Stats> getPerNodes() {
        return per_nodes;
    }

    public int getMissing() {
        return missing;
    }

    public Collection<Long> getMessageRate() {
        return message_rate;
    }

    public Collection<Long> getByteRate() {
        return byte_rate;
    }
}
