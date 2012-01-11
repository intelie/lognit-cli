package net.intelie.lognit.cli.commands;

public interface Command {
    String name();
    void execute(String... args) throws Exception;
}
