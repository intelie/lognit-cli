package net.intelie.lognit.cli.input;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ArgsParserTest {
    @Test
    public void willGetOptionalValue() throws Exception {
        ArgsParser args = new ArgsParser("-a", "abc", "-e", "123");
        assertThat(args.option(Integer.class, "-e")).isEqualTo(123);
        assertThat(args).isEqualTo(new ArgsParser("-a", "abc"));
    }

    @Test
    public void willGetTheFirstOneOfOptionalValues() throws Exception {
        ArgsParser args = new ArgsParser("-a", "abc", "-e", "123", "--ew", "234");
        assertThat(args.option(Integer.class, "--ew", "-e")).isEqualTo(123);
        assertThat(args).isEqualTo(new ArgsParser("-a", "abc", "--ew", "234"));
    }

    @Test
    public void willGetText() throws Exception {
        ArgsParser args = new ArgsParser("-a", "abc", "-e", "123");
        assertThat(args.option(Integer.class, "-e")).isEqualTo(123);
        assertThat(args.text()).isEqualTo("-a abc");
    }

    @Test
    public void willGetTextWithEmptyEntries() throws Exception {
        ArgsParser args = new ArgsParser("", " ", "   ", null);
        assertThat(args.text()).isEqualTo("");
    }

    @Test
    public void whenOptionalDoesntExist() throws Exception {
        ArgsParser args = new ArgsParser();

        assertThat(args.option(Integer.class, "-e")).isEqualTo(null);
    }

    @Test
    public void whenOptionalExistsWithNoValue() throws Exception {
        ArgsParser args = new ArgsParser("-e");

        assertThat(args.option(Integer.class, "-e")).isEqualTo(null);

    }

    @Test
    public void whenOptionalExistsWithWrongValue() throws Exception {
        ArgsParser args = new ArgsParser("-e", "abc");

        assertThat(args.option(Integer.class, "-e")).isEqualTo(null);
    }

    @Test
    public void canConsumeFlag() throws Exception {
        ArgsParser args = new ArgsParser("-e", "-a");

        assertThat(args.flag("-e")).isEqualTo(true);
        assertThat(args).isEqualTo(new ArgsParser("-a"));
        assertThat(args.flag("-c")).isEqualTo(false);
        assertThat(args).isEqualTo(new ArgsParser("-a"));
    }

    @Test
    public void canConsumeOneOfFlags() throws Exception {
        ArgsParser args = new ArgsParser("-e", "-a", "-c");

        assertThat(args.flag("-a", "-c")).isEqualTo(true);
        assertThat(args).isEqualTo(new ArgsParser("-e", "-c"));
        assertThat(args.flag("-a", "-c")).isEqualTo(true);
        assertThat(args).isEqualTo(new ArgsParser("-e"));
        assertThat(args.flag("-a", "-c")).isEqualTo(false);
        assertThat(args).isEqualTo(new ArgsParser("-e"));
    }


    @Test
    public void whenAreEqual() throws Exception {
        ArgsParser args1 = new ArgsParser("-e", "-a");
        ArgsParser args2 = new ArgsParser("-e", "-a");

        assertThat((Object) args1).isEqualTo(args2);
        assertThat(args1.hashCode()).isEqualTo(args2.hashCode());
    }

    @Test
    public void whenAreDifferent() throws Exception {
        ArgsParser args1 = new ArgsParser("-e", "-a");
        ArgsParser args2 = new ArgsParser("-e", "-b");

        assertThat((Object) args1).isNotEqualTo(args2);
        assertThat((Object) args1).isNotEqualTo(new Object());

        assertThat(args1.hashCode()).isNotEqualTo(args2.hashCode());
        assertThat(args1.hashCode()).isNotEqualTo(new Object().hashCode());
    }
}
