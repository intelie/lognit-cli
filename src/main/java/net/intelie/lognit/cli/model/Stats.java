package net.intelie.lognit.cli.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;

public class Stats implements Serializable {
    private final String node;
    private final long total_docs;
    private final Collection<String> queries;

    public Stats(String node, long totalDocs, Collection<String> queries) {
        this.node = node;
        this.total_docs = totalDocs;
        this.queries = queries;
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
}
