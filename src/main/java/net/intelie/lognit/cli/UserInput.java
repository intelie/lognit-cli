package net.intelie.lognit.cli;

import com.google.inject.Inject;
import jline.ConsoleReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UserInput {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ConsoleReader console;

    @Inject
    public UserInput(ConsoleReader console) {
        this.console = console;
    }

    public void printf(String format, Object... args) {
        try {
            console.putString(String.format(format, args));
            console.flushConsole();
        } catch (IOException e) {
            logger.warn("why fail printf?", e);
        }
    }

    public String readLine(String prompt) {
        try {
            return console.readLine(prompt);
        } catch (IOException e) {
            logger.warn("why fail readline?", e);
            return "";
        }
    }

    public String readPassword(String prompt) {
        try {
            return console.readLine(prompt, '\0');
        } catch (IOException e) {
            logger.warn("why fail readPassword?", e);

            return "";
        }
    }
}
