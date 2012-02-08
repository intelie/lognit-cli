package net.intelie.lognit.cli.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;

public class Stats implements Serializable {
    private final String node;
    private final long total_docs;
    private final Collection<String> queries;
    private final Collection<Long> load;

    public Stats(String node, long totalDocs, Collection<String> queries, Collection<Long> load) {
        this.node = node;
        this.total_docs = totalDocs;
        this.queries = queries;
        this.load = load;
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

    public Collection<Long> getLoad() {
        return load;
    }
}
