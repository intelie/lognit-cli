package net.intelie.lognit.cli.input;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ArgsParserTest {
    @Test
    public void commandNameIsTheFirstString() throws Exception {
        ArgsParser args = new ArgsParser("abc", "-e");
        assertThat(args.commandName()).isEqualTo("abc");
        assertThat(args).containsOnly("-e");
    }

    @Test
    public void commandNameIsRequiredAndMustBeFirst() throws Exception {
        ArgsParser args = new ArgsParser("-e");

        try {
            args.commandName();
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(ArgsParseException.commandNameRequired().getMessage());
        }
    }

    @Test
    public void commandNameIsRequired() throws Exception {
        ArgsParser args = new ArgsParser();

        try {
            args.commandName();
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(ArgsParseException.commandNameRequired().getMessage());
        }
    }

    @Test
    public void willGetRequiredValue() throws Exception {
        ArgsParser args = new ArgsParser("-a", "abc", "-e", "123");
        assertThat(args.required(Integer.class, "e")).isEqualTo(123);
        assertThat(args).containsOnly("-a", "abc");
    }

    @Test
    public void whenRequiredDoesntExist() throws Exception {
        ArgsParser args = new ArgsParser();

        try {
            args.required(Integer.class, "e");
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(ArgsParseException.optionRequired("e").getMessage());
        }
    }

    @Test
    public void whenRequiredExistsWithNoValue() throws Exception {
        ArgsParser args = new ArgsParser("-e");

        try {
            args.required(Integer.class, "e");
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(ArgsParseException.optionRequired("e").getMessage());
        }
    }

    @Test
    public void whenRequiredExistsWithWrongValue() throws Exception {
        ArgsParser args = new ArgsParser("-e", "abc");

        try {
            args.required(Integer.class, "e");
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(
                    ArgsParseException.optionNotConvertible(new InvocationTargetException(null)).getMessage());
        }
    }

    @Test
    public void willGetOptionalValue() throws Exception {
        ArgsParser args = new ArgsParser("-a", "abc", "-e", "123");
        assertThat(args.optional(Integer.class, "e", 42)).isEqualTo(123);
        assertThat(args).containsOnly("-a", "abc");
    }

    @Test
    public void whenOptionalDoesntExist() throws Exception {
        ArgsParser args = new ArgsParser();

        assertThat(args.optional(Integer.class, "e", 42)).isEqualTo(42);
    }

    @Test
    public void whenOptionalExistsWithNoValue() throws Exception {
        ArgsParser args = new ArgsParser("-e");

        assertThat(args.optional(Integer.class, "e", 42)).isEqualTo(42);

    }

    @Test
    public void whenOptionalExistsWithWrongValue() throws Exception {
        ArgsParser args = new ArgsParser("-e", "abc");

        try {
            args.optional(Integer.class, "e", 42);
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(
                    ArgsParseException.optionNotConvertible(new InvocationTargetException(null)).getMessage());
        }
    }

    @Test
    public void checkingEmptyOnEmptyParser() throws Exception {
        ArgsParser args = new ArgsParser();
        args.checkEmpty();
    }

    @Test
    public void checkingEmptyOnNonEmptyParser() throws Exception {
        ArgsParser args = new ArgsParser("-a", "123");
        try {
            args.checkEmpty();
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(
                    ArgsParseException.unexpectedParameters(Arrays.asList("-a", "123")).getMessage());
        }
    }

    @Test
    public void canConsumeFlag() throws Exception {
        ArgsParser args = new ArgsParser("-e", "-a");

        assertThat(args.flag("e")).isEqualTo(true);
        assertThat(args).containsOnly("-a");
        assertThat(args.flag("c")).isEqualTo(false);
        assertThat(args).containsOnly("-a");
    }

    @Test
    public void whenAreEqual() throws Exception {
        ArgsParser args1 = new ArgsParser("-e", "-a");
        ArgsParser args2 = new ArgsParser("-e", "-a");

        assertThat((Object)args1).isEqualTo(args2);
        assertThat(args1.hashCode()).isEqualTo(args2.hashCode());
    }

    @Test
    public void whenAreDifferent() throws Exception {
        ArgsParser args1 = new ArgsParser("-e", "-a");
        ArgsParser args2 = new ArgsParser("-e", "-b");

        assertThat((Object)args1).isNotEqualTo(args2);
        assertThat((Object)args1).isNotEqualTo(new Object());

        assertThat(args1.hashCode()).isNotEqualTo(args2.hashCode());
        assertThat(args1.hashCode()).isNotEqualTo(new Object().hashCode());
    }
}
