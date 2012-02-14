package net.intelie.lognit.cli.model;

import java.util.Collection;

public class StatsSummary {
    private final long total_docs;
    private final Collection<String> nodes;
    private final Collection<String> queries;
    private final Collection<Stats> per_nodes;
    private final Collection<Long> docs_rate;
    private final Collection<Long> bytes_rate;
    private int missing;

    public StatsSummary(long total_docs, Collection<String> nodes, Collection<String> queries, Collection<Stats> per_nodes, Collection<Long> docs_rate, Collection<Long> bytes_rate, int missing) {
        this.total_docs = total_docs;
        this.nodes = nodes;
        this.queries = queries;
        this.per_nodes = per_nodes;
        this.docs_rate = docs_rate;
        this.bytes_rate = bytes_rate;
        this.missing = missing;
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

    public Collection<Long> getDocsRate() {
        return docs_rate;
    }

    public Collection<Long> getBytesRate() {
        return bytes_rate;
    }
}
