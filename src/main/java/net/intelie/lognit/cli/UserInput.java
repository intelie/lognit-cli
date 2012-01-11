package net.intelie.lognit.cli;

import com.google.inject.Inject;

import java.io.Console;

public class UserInput {
    private final Console console;

    @Inject
    public UserInput(Console console) {
        this.console = console;
    }

    public void printf(String format, Object... args) {
        console.printf(format, args);
    }

    public String readLine() {
        return console.readLine();
    }

    public String readPassword() {
        return new String(console.readPassword());
    }
}
