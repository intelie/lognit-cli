package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.Lognit;
import net.intelie.lognit.cli.model.SearchChannel;

public class SearchCommand implements Command {
    private final UserInput console;
    private final Lognit lognit;

    @Inject
    public SearchCommand(UserInput console, Lognit lognit) {
        this.console = console;
        this.lognit = lognit;
    }

    @Override
    public String name() {
        return "search";
    }

    @Override
    public void execute(ArgsParser parser) throws Exception {
        String query = parser.required(String.class);

        SearchChannel channel = lognit.beginSearch(query, null);
        console.println(channel.getChannel());
    }
}

