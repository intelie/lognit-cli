package net.intelie.lognit.cli.input;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class RequestOptionsTest {
    @Test
    public void canConstructWithDefaults() {
        RequestOptions opts = new RequestOptions(null, null, null, null, null, null, false);
        assertThat(opts.getServer()).isEqualTo(null);
        assertThat(opts.getUser()).isEqualTo(null);
        assertThat(opts.getPassword()).isEqualTo(null);
        assertThat(opts.getQuery()).isEqualTo(null);
        assertThat(opts.hasQuery()).isEqualTo(false);
        assertThat(opts.getLines()).isEqualTo(20);
        assertThat(opts.getTimeout()).isEqualTo(10);
        assertThat(opts.getTimeoutInMilliseconds()).isEqualTo(10000);
        assertThat(opts.isFollow()).isEqualTo(false);
    }

    @Test
    public void canConstructWithNonDefaults() {
        RequestOptions opts = new RequestOptions("A", "B", "C", "D", 42, 43, true);
        assertThat(opts.getServer()).isEqualTo("A");
        assertThat(opts.getUser()).isEqualTo("B");
        assertThat(opts.getPassword()).isEqualTo("C");
        assertThat(opts.getQuery()).isEqualTo("D");
        assertThat(opts.hasQuery()).isEqualTo(true);
        assertThat(opts.getLines()).isEqualTo(43);
        assertThat(opts.getTimeout()).isEqualTo(42);
        assertThat(opts.getTimeoutInMilliseconds()).isEqualTo(42000);
        assertThat(opts.isFollow()).isEqualTo(true);
    }
}
