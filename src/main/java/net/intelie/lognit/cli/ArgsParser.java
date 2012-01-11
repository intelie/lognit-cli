package net.intelie.lognit.cli;

import com.google.inject.Inject;
import net.intelie.lognit.cli.commands.Command;
import net.intelie.lognit.cli.state.StateKeeper;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ArgsParser {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserInput console;
    private final StateKeeper state;
    private final Map<String, Command> commands;

    @Inject
    public ArgsParser(UserInput console, StateKeeper state, Command... commands) {
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

    public void run(String... args) throws Exception {
        state.begin();

        try {
            if (args.length > 0) {
                Command command = commands.get(args[0]);
                if (command == null) {
                    printUsage();
                } else {
                    command.execute((String[]) ArrayUtils.subarray(args, 1, args.length));
                }
            } else {
                printUsage();
            }

        } catch (Exception ex) {
            logger.warn("An error has ocurred. Sad.", ex);
            console.printf("%s: %s\n", ex.getClass().getSimpleName(), ex.getMessage());
        } finally {
            state.end();
        }
    }

    private void printUsage() {
        console.printf("no such command\n");
        console.printf("available commands: %s\n", StringUtils.join(commands.keySet(), ", "));
    }
}
