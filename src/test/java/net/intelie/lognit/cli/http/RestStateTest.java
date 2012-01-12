package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.Cookie;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class RestStateTest {
    @Test
    public void willExposeGetters() {
        Cookie[] cookies = new Cookie[0];

        RestState state = new RestState(cookies, "abc");
        assertThat(state.getCookies()).isSameAs(cookies);
        assertThat(state.getServer()).isEqualTo("abc");
    }
}
