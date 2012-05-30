package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.Message;

public interface Formatter {
    void printStatus(String format, Object... args);

    void printMessage(Message message);

    void printAggregated(Aggregated aggregated);
}
