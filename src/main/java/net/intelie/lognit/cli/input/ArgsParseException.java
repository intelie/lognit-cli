package net.intelie.lognit.cli.input;

public class ArgsParseException extends Exception {
    public static ArgsParseException commandNameRequired() {
        return new ArgsParseException("the command name is required");
    }

    public static ArgsParseException optionRequired(String optionName) {
        return new ArgsParseException("the option '-" + optionName + "' is required for this command");
    }

    public static ArgsParseException optionNotConvertible(Exception e) {
        return new ArgsParseException(
                String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage()), e);
    }


    public ArgsParseException(String message) {
        super(message);
    }

    public ArgsParseException(String message, Throwable inner) {
        super(message, inner);
    }
}
