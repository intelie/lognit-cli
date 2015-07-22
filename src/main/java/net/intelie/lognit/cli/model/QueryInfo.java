package net.intelie.lognit.cli.model;

public class QueryInfo {
    private final boolean valid;
    private final String message;

    public QueryInfo(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public boolean valid() {
        return valid;
    }

    public String message() {
        return message;
    }
}
