package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.UserOptions;

public class RetryConnectionException extends Exception {
    private final UserOptions options;

    public RetryConnectionException(UserOptions options, String message) {
        super(message);
        this.options = options;
    }

    public RetryConnectionException(UserOptions options, Throwable e) {
        super(e.getMessage(), e);
        this.options = options;
    }


    public UserOptions options() {
        return options;
    }

}
