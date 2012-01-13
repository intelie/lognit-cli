package net.intelie.lognit.cli.input;

import org.apache.commons.lang.StringUtils;

import java.util.List;

public class ArgsParseException extends Exception {
    public static ArgsParseException commandNameRequired() {
        return new ArgsParseException("the command name is required");
    }

    public static ArgsParseException commandNotFound(String command) {
        return new ArgsParseException("the command '" + command + "' doesn't exist");
    }


    public static ArgsParseException optionRequired(String optionName) {
        return new ArgsParseException("the option '-" + optionName + "' is required in this context");
    }

    public static ArgsParseException optionNotConvertible(Exception e) {
        return new ArgsParseException(
                String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage()), e);
    }

    public static ArgsParseException unexpectedParameters(Iterable<String> list) {
        return new ArgsParseException(String.format("unexpected: %s", StringUtils.join(list.iterator(), " ")));
    }

    public ArgsParseException(String message) {
        super(message);
    }

    public ArgsParseException(String message, Throwable inner) {
        super(message, inner);
    }


}
