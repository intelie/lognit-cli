package net.intelie.lognit.cli;

import jline.ConsoleReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

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
            format = String.format(Locale.ENGLISH, format, args);
        return format;
    }

    public boolean isTTY() {
        return System.console() != null && console.getTerminal().isANSISupported();
    }

    public synchronized char waitChar(char... allowed) {
        try {
            return (char) console.readCharacter(allowed);
        } catch (IOException e) {
            return '\0';
        }
    }

    public synchronized void printStill(String format, Object... args) {
        try {
            console.setDefaultPrompt(null);
            console.setCursorPosition(0);
            console.killLine();
            console.getCursorBuffer().clearBuffer();
            console.putString(reallyFormat(format, args));
            console.flushConsole();
        } catch (IOException e) {
        }
    }

    public synchronized void fixCursor() {
        try {
            if (console.getCursorBuffer().cursor != 0)
                console.printNewline();
            console.getCursorBuffer().clearBuffer();
        } catch (IOException e) {
        }
    }

    public synchronized void registerFix(Runtime runtime) {
        runtime.addShutdownHook(new Thread() {
            @Override
            public void run() {
                fixCursor();
            }
        });
    }

    public synchronized void println(String format, Object... args) {
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
