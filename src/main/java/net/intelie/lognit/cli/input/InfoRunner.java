package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.model.Stats;
import net.intelie.lognit.cli.model.StatsSummary;

import java.util.Collection;

public class InfoRunner {
    public static final String HAS_MISSING_NODES = "(%s): %d node(s) did not respond";
    public static final String NO_MISSING_NODES = "(%s): all nodes responded";
    public static final String NODE_TEXT = "node '%s':";
    public static final String SUMMARY_TEXT = "SUMMARY:";
    public static final String INFO_TEXT = "  info: %,d queries / %,d docs";
    public static final String DOCS_TEXT =  "  docs/s: %,d (3s) / %,d (15s) / %,d (60s)";
    public static final String BYTES_TEXT = "  bytes/s: %.2f MB (3s) / %.2f MB (15s) / %.2f MB (60s)";


    private final UserConsole console;

    @Inject
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
            console.printOut(NODE_TEXT, stats.getNode());
            console.printOut(INFO_TEXT, stats.getQueries().size(), stats.getTotalDocs());
            printLoad(DOCS_TEXT, stats.getDocsRate(), 1);
            printLoad(BYTES_TEXT, stats.getBytesRate(), 1024 * 1024);
        }

        console.printOut("");
        console.printOut(SUMMARY_TEXT);
        console.printOut(INFO_TEXT, summary.getQueries().size(), summary.getTotalDocs());
        printLoad(DOCS_TEXT, summary.getDocsRate(), 1);
        printLoad(BYTES_TEXT, summary.getBytesRate(), 1024 * 1024);
    }

    private void printLoad(String text, Collection<Long> load, double factor) {
        if (load == null) return;

        long v3 = 0, v15 = 0, v60 = 0;
        int i = 0;
        for (Long value : load) {
            if (i < 3) v3 += value;
            if (i < 15) v15 += value;
            if (i < 60) v60 += value;
            i++;
        }
        if (factor == 1)
            console.printOut(text, v3 / 3, v15 / 15, v60 / 60);
        else
            console.printOut(text, v3 / 3 / factor, v15 / 15 / factor, v60 / 60 / factor);
    }
}