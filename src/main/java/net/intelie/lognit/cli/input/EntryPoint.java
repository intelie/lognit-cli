package net.intelie.lognit.cli.input;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import net.intelie.lognit.cli.state.StateKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class EntryPoint {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserInput console;
    private final StateKeeper state;
    private final Map<String, Command> commands;

    @Inject
    public EntryPoint(UserInput console, StateKeeper state, Command... commands) {
        this.console = console;
        this.state = state;
        this.commands = makeCommands(commands);
    }

    private HashMap<String, Command> makeCommands(Command[] commands) {
        HashMap<String, Command> map = new HashMap<String, Command>();
        for (Command command : commands)
            map.put(command.name(), command);
        return map;
    }

    public void run(String... args) {
        ArgsParser parser = new ArgsParser(args);
        state.begin();

        try {
            String commandName = parser.commandName();
            Command command = commands.get(commandName);
            if (command == null)
                throw ArgsParseException.commandNotFound(commandName);
            command.execute(parser);
        } catch (ArgsParseException ex) {
            console.println("args: " + ex.getMessage());
            printUsage();
        } catch (Exception ex) {
            logger.warn("An error has ocurred. Sad.", ex);
            console.println("%s: %s", ex.getClass().getSimpleName(), ex.getMessage());
        } finally {
            state.end();
        }
    }

    private void printUsage() {
        try {
            console.println(Resources.toString(Resources.getResource("usage.txt"), Charset.defaultCharset()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
