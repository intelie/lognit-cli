package net.intelie.lognit.cli.input;

public interface Command {
    String name();
    void execute(String... args) throws Exception;
}
