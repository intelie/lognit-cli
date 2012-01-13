package net.intelie.lognit.cli.input;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ArgsTest {
    @Test
    public void commandNameIsTheFirstString() throws Exception {
        Args args = new Args("abc", "-e");
        assertThat(args.commandName()).isEqualTo("abc");
        assertThat(args).containsOnly("-e");
    }

    @Test
    public void commandNameIsRequiredAndMustBeFirst() throws Exception {
        Args args = new Args("-e");

        try {
            args.commandName();
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(ArgsParseException.commandNameRequired().getMessage());
        }
    }

    @Test
    public void commandNameIsRequired() throws Exception {
        Args args = new Args();

        try {
            args.commandName();
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(ArgsParseException.commandNameRequired().getMessage());
        }
    }

    @Test
    public void willGetRequiredValue() throws Exception {
        Args args = new Args("-a", "abc", "-e", "123");
        assertThat(args.required(Integer.class, "e")).isEqualTo(123);
        assertThat(args).containsOnly("-a", "abc");
    }

    @Test
    public void whenRequiredDoesntExist() throws Exception {
        Args args = new Args();

        try {
            args.required(Integer.class, "e");
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(ArgsParseException.optionRequired("e").getMessage());
        }
    }

    @Test
    public void whenRequiredExistsWithNoValue() throws Exception {
        Args args = new Args("-e");

        try {
            args.required(Integer.class, "e");
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(ArgsParseException.optionRequired("e").getMessage());
        }
    }

    @Test
    public void whenRequiredExistsWithWrongValue() throws Exception {
        Args args = new Args("-e", "abc");

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
        Args args = new Args("-a", "abc", "-e", "123");
        assertThat(args.optional(Integer.class, "e", 42)).isEqualTo(123);
        assertThat(args).containsOnly("-a", "abc");
    }

    @Test
    public void whenOptionalDoesntExist() throws Exception {
        Args args = new Args();

        assertThat(args.optional(Integer.class, "e", 42)).isEqualTo(42);
    }

    @Test
    public void whenOptionalExistsWithNoValue() throws Exception {
        Args args = new Args("-e");

        assertThat(args.optional(Integer.class, "e", 42)).isEqualTo(42);

    }

    @Test
    public void whenOptionalExistsWithWrongValue() throws Exception {
        Args args = new Args("-e", "abc");

        try {
            args.optional(Integer.class, "e", 42);
            fail("should throw");
        } catch (ArgsParseException e) {
            assertThat(e.getMessage()).isEqualTo(
                    ArgsParseException.optionNotConvertible(new InvocationTargetException(null)).getMessage());
        }
    }

    @Test
    public void canConsumeFlag() throws Exception {
        Args args = new Args("-e", "-a");

        assertThat(args.flag("e")).isEqualTo(true);
        assertThat(args).containsOnly("-a");
        assertThat(args.flag("c")).isEqualTo(false);
        assertThat(args).containsOnly("-a");
    }
}
