package net.intelie.lognit.cli.model;

import java.io.Serializable;
import java.util.Collection;

public class Stats implements Serializable {
    private final String node;
    private final long total_docs;
    private final Collection<String> queries;
    private final Collection<Long> docs_rate;
    private final Collection<Long> bytes_rate;

    public Stats(String node, long totalDocs, Collection<String> queries, Collection<Long> docs_rate, Collection<Long> bytes_rate) {
        this.node = node;
        this.total_docs = totalDocs;
        this.queries = queries;
        this.docs_rate = docs_rate;
        this.bytes_rate = bytes_rate;
    }

    public String getNode() {
        return node;
    }

    public long getTotalDocs() {
        return total_docs;
    }

    public Collection<String> getQueries() {
        return queries;
    }

    public Collection<Long> getDocsRate() {
        return docs_rate;
    }

    public Collection<Long> getBytesRate() {
        return bytes_rate;
    }
}
