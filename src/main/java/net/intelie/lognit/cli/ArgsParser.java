package net.intelie.lognit.cli;

import com.google.common.base.Objects;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class ArgsParser {
    private final LinkedList<String> args;

    public ArgsParser(String... args) {
        this.args = new LinkedList<String>();
        Collections.addAll(this.args, args);
    }

    public boolean flag(String... flag) {
        return findIterator(flag) != null;
    }

    public <T> T option(Class<T> type, String... options) {
        String value = findValue(options);
        if (value == null) return null;
        return convert(type, value);
    }

    private <T> T convert(Class<T> type, String value) {
        try {
            return type.getConstructor(String.class).newInstance(value);
        } catch (Exception e) {
            return null;
        }
    }

    private String findValue(String... options) {
        ListIterator<String> it = findIterator(options);
        return removeNext(it);
    }

    private ListIterator<String> findIterator(String... options) {
        List<String> optionList = Arrays.asList(options);
        ListIterator<String> it = args.listIterator();
        while (it.hasNext()) {
            String key = it.next();
            if (optionList.contains(key)) {
                it.remove();
                return it;
            }
        }
        return null;
    }

    private String removeNext(ListIterator<String> it) {
        if (it == null || !it.hasNext()) return null;
        String value = it.next();
        it.remove();
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ArgsParser)) return false;
        ArgsParser that = (ArgsParser) o;

        return Objects.equal(this.args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(args);
    }

    public String text() {
        return StringUtils.join(args, " ").trim();
    }
}
