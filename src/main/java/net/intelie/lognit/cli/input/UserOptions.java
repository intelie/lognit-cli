package net.intelie.lognit.cli.input;

import com.google.common.base.Strings;

public class UserOptions {
    private boolean help;
    private final String server;
    private final String user;
    private final String pass;
    private final String query;

    public UserOptions(String... args) {
        ArgsParser parser = new ArgsParser(args);
        help = parser.flag("-?", "--help");
        server = parser.option(String.class, "-s", "--server");
        user = parser.option(String.class, "-u", "--user");
        pass = parser.option(String.class, "-p", "--pass");
        query = parser.text();
    }

    public boolean isHelp() {
        return help;
    }

    public boolean mustAuthenticate() {
        return server != null;
    }

    public String getServer() {
        return server;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public boolean hasQuery() {
        return !Strings.isNullOrEmpty(query);
    }

    public String getQuery() {
        return query;
    }

}
