package net.intelie.lognit.cli.input;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Args implements Iterable<String> {
    private final LinkedList<String> args;

    public Args(String... args) {
        this.args = new LinkedList<String>();
        Collections.addAll(this.args, args);
    }

    public String commandName() throws ArgsParseException {
        String cmd = args.pollFirst();
        if (cmd == null || cmd.startsWith("-"))
            throw ArgsParseException.commandNameRequired();
        return cmd;
    }

    public <T> T required(Class<T> type, String option) throws ArgsParseException {
        String value = findValue(option);
        if (value == null)
            throw ArgsParseException.optionRequired(option);
        return convert(type, value);
    }

    public <T> T optional(Class<T> type, String option, T defaultValue) throws ArgsParseException {
        String value = findValue(option);
        if (value == null)
            return defaultValue;
        return convert(type, value);
    }

    private <T> T convert(Class<T> type, String value) throws ArgsParseException {
        try {
            return type.getConstructor(String.class).newInstance(value);
        } catch (Exception e) {
            throw ArgsParseException.optionNotConvertible(e);
        }
    }

    private String findValue(String option) throws ArgsParseException {
        int index = args.indexOf("-" + option);
        if (index == -1) return null;

        ListIterator<String> it = args.listIterator();
        while (index-- > 0) it.next();
        removeNext(it);
        return removeNext(it);
    }

    private String removeNext(ListIterator<String> it) {
        if (!it.hasNext()) return null;
        String value = it.next();
        it.remove();
        return value;
    }

    @Override
    public Iterator<String> iterator() {
        return args.iterator();
    }

    public boolean flag(String flag) {
        return args.remove("-" + flag);
    }
}
