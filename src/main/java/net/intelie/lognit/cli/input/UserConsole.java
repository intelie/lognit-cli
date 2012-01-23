package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import jline.ConsoleReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

public class UserConsole {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ConsoleReader console;
    private final PrintWriter stdout;

    @Inject
    public UserConsole(ConsoleReader console, PrintWriter stdout) {
        this.console = console;
        this.stdout = stdout;
    }

    public void printOut(String format, Object... args) {
        stdout.println(String.format(format, args));
        stdout.flush();
    }
    
    public void println(String format, Object... args) {
        try {
            console.printString(String.format(format, args));
            console.printNewline();
            console.flushConsole();
        } catch (IOException e) {
            logger.warn("why fail println?", e);
        }
    }

    public String readLine(String format, Object... args) {
        try {
            return console.readLine(String.format(format, args));
        } catch (IOException e) {
            logger.warn("why fail readline?", e);
            return "";
        }
    }

    public String readPassword(String format, Object... args) {
        try {
            return console.readLine(String.format(format, args), '\0');
        } catch (IOException e) {
            logger.warn("why fail readPassword?", e);

            return "";
        }
    }
}
