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
    private int missing;

    public StatsSummary(Iterable<Stats> stats, int missing) {
        this.missing = missing;
        long totalDocs = 0;
        nodes = new ArrayList<String>();
        queries = new TreeSet<String>();
        per_nodes = Lists.newArrayList(stats);

        for (Stats stat : stats) {
            totalDocs += stat.getTotalDocs();
            this.nodes.add(stat.getNode());
            this.queries.addAll(stat.getQueries());
        }

        this.total_docs = totalDocs;
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
}
