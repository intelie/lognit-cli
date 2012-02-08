package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.model.Stats;
import net.intelie.lognit.cli.model.StatsSummary;

import java.util.Collection;

public class InfoRunner {
    public static final String HAS_MISSING_NODES = "(%s): %d node(s) did not respond";
    public static final String NO_MISSING_NODES = "(%s): all nodes responded";
    public static final String NODE_INFO = "node '%s':";
    public static final String TOTAL_INFO = "SUMMARY:";
    public static final String STATS_INFO = "  stat: %d queries / %d docs";
    public static final String LOAD_INFO =  "  rate: %d (3s) / %d (15s) / %d (60s)";


    private final UserConsole console;

    public InfoRunner(UserConsole console) {
        this.console = console;
    }

    public void printInfo(String server, StatsSummary summary) {
        if (summary.getMissing() > 0)
            console.printOut(HAS_MISSING_NODES, server, summary.getMissing());
        else
            console.printOut(NO_MISSING_NODES, server);

        for (Stats stats : summary.getPerNodes()) {
            console.printOut("");
            console.printOut(NODE_INFO, stats.getNode());
            console.printOut(STATS_INFO, stats.getQueries().size(), stats.getTotalDocs());
            printLoad(stats.getLoad());
        }

        console.printOut("");
        console.printOut(TOTAL_INFO);
        console.printOut(STATS_INFO, summary.getQueries().size(), summary.getTotalDocs());
        printLoad(summary.getLoad());
    }

    private void printLoad(Collection<Long> load) {
        if (load == null) return;

        long v3 = 0, v15 = 0, v60 = 0;
        int i = 0;
        for (Long value : load) {
            if (i < 3) v3 += value;
            if (i < 15) v15 += value;
            if (i < 70) v60 += value;
            i++;
        }
        console.printOut(LOAD_INFO, v3/3, v15/15, v60/60);
    }
}