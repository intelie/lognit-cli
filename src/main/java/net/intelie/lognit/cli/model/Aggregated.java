package net.intelie.lognit.cli.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class Aggregated extends ArrayList<AggregatedItem> {
    public Aggregated() {
    }

    public Aggregated(AggregatedItem... items) {
        super(Arrays.asList(items));
    }
}

