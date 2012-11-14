package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.SearchStats;

public interface Formatter {
    void printStatus(String format, Object... args);

    void print(Message message, boolean withMetadata);

    void print(Aggregated aggregated);

    void print(SearchStats stats);
}
