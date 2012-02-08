package net.intelie.lognit.cli.model;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class StatsSummary {
    private final long total_docs;
    private final Collection<String> nodes;
    private final Collection<String> queries;
    private final Collection<Stats> per_nodes;
    private final Collection<Long> load;
    private int missing;

    public StatsSummary(long total_docs, Collection<String> nodes, Collection<String> queries, Collection<Stats> per_nodes, Collection<Long> load, int missing) {
        this.total_docs = total_docs;
        this.nodes = nodes;
        this.queries = queries;
        this.per_nodes = per_nodes;
        this.load = load;
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

    public Collection<Long> getLoad() {
        return load;
    }
}
