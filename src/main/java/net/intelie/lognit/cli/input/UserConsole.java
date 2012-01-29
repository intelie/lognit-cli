package net.intelie.lognit.cli.input;

import jline.ConsoleReader;

import java.io.IOException;
import java.io.PrintWriter;

public class UserConsole {
    private final ConsoleReader console;
    private final PrintWriter stdout;

    public UserConsole(ConsoleReader console, PrintWriter stdout) {
        this.console = console;
        this.stdout = stdout;
    }

    public void printOut(String format, Object... args) {
        stdout.println(reallyFormat(format, args));
        stdout.flush();
    }

    private String reallyFormat(String format, Object[] args) {
        if (args.length > 0)
            format = String.format(format, args);
        return format;
    }

    public boolean isTTY() {
        return System.console() != null && console.getTerminal().isANSISupported();
    }

    public char waitChar(char... allowed) {
        try {
            return (char) console.readCharacter(allowed);
        } catch (IOException e) {
            return '\0';
        }
    }

    public void println(String format, Object... args) {
        try {
            console.printString(reallyFormat(format, args));
            console.printNewline();
            console.flushConsole();
        } catch (IOException e) {
        }
    }

    public String readLine(String format, Object... args) {
        try {
            return console.readLine(reallyFormat(format, args));
        } catch (IOException e) {
            return "";
        }
    }

    public String readPassword(String format, Object... args) {
        try {
            return console.readLine(reallyFormat(format, args), '\0');
        } catch (IOException e) {
            return "";
        }
    }
}
