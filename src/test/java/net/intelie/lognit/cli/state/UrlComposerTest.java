package net.intelie.lognit.cli.state;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UrlComposerTest {
    @Test(expected = RuntimeException.class)
    public void withoutSlashInStartWillThrowRuntimeException() {
        HttpClient client = mock(HttpClient.class, RETURNS_DEEP_STUBS);
        when(client.getState().getCookies()).thenReturn(newCookies());

        UrlComposer composer = new UrlComposer(client);
        composer.deriveFullUrl("abc/qwe");
    }

    @Test
    public void willPutHttpInFrontOfDomainWithSlashInStart() {
        HttpClient client = mock(HttpClient.class, RETURNS_DEEP_STUBS);
        when(client.getState().getCookies()).thenReturn(newCookies());

        UrlComposer composer = new UrlComposer(client);
        assertThat(composer.deriveFullUrl("/abc/qwe")).isEqualTo("http://a/abc/qwe");
    }


    @Test(expected = RuntimeException.class)
    public void noCookiesThrowRuntimeException() {
        HttpClient client = mock(HttpClient.class, RETURNS_DEEP_STUBS);

        UrlComposer composer = new UrlComposer(client);
        composer.deriveFullUrl("/abc/qwe");
    }

    private Cookie[] newCookies() {
        return new Cookie[]{new Cookie("A", "B", "C"), new Cookie("D", "E", "F")};
    }
}
