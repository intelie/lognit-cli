package net.intelie.lognit.cli.input;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class UserOptionsTest {
    @Test
    public void whenParsingHelp() throws Exception {
        UserOptions options = new UserOptions("-a", "-b", "-?");
        assertThat(options.isHelp()).isTrue();
        assertThat(options.getQuery()).isEqualTo("-a -b");
    }

    @Test
    public void whenParsingHelpAlone() throws Exception {
        UserOptions options = new UserOptions("-?");
        assertThat(options.isHelp()).isTrue();
        assertThat(options.hasQuery()).isFalse();
    }

    @Test
    public void whenParsingServerRequest() throws Exception {
        UserOptions options = new UserOptions("some query no one will ever", "-s", "localhost", "notice");
        assertThat(options.isHelp()).isFalse();
        assertThat(options.mustAuthenticate()).isTrue();
        assertThat(options.getServer()).isEqualTo("localhost");
        assertThat(options.getUser()).isNull();
        assertThat(options.getPass()).isNull();

        assertThat(options.hasQuery()).isTrue();
        assertThat(options.getQuery()).isEqualTo("some query no one will ever notice");
    }

    @Test
    public void whenParsingServerRequestWithUser() throws Exception {
        UserOptions options = new UserOptions("-u","user", "-s", "localhost", "-p", "whatever", "", "");
        assertThat(options.isHelp()).isFalse();

        assertThat(options.mustAuthenticate()).isTrue();
        assertThat(options.getServer()).isEqualTo("localhost");
        assertThat(options.getUser()).isEqualTo("user");
        assertThat(options.getPass()).isEqualTo("whatever");

        assertThat(options.hasQuery()).isFalse();
        assertThat(options.getQuery()).isEqualTo("");
    }


}
