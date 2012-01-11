package net.intelie.lognit.cli.state;

import com.google.inject.Inject;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlComposer {
    private final HttpClient client;

    @Inject
    public UrlComposer(HttpClient client) {
        this.client = client;
    }

    public String deriveFullUrl(String relativeUrl) {
        try {
            Cookie cookie = client.getState().getCookies()[0];
            return new URI("http", cookie.getDomain(), relativeUrl, null).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
