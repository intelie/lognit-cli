package net.intelie.lognit.cli;

import net.intelie.lognit.cli.model.AggregatedItem;

public class AggregatedItemHelper {
    public static AggregatedItem map(Object... values) {
        AggregatedItem map = new AggregatedItem();
        for (int i = 0; i + 1 < values.length; i += 2) {
            map.put((String) values[i], values[i + 1]);
        }
        return map;
    }
}