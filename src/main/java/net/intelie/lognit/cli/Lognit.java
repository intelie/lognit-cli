package net.intelie.lognit.cli;

import com.google.inject.Inject;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.model.SearchChannel;
import net.intelie.lognit.cli.model.SearchListener;
import net.intelie.lognit.cli.model.Welcome;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Lognit {
    public static final String URL_WELCOME = "/rest/users/welcome";
    public static final String URL_SEARCH = "/rest/search?expression=%s";
    private final RestClient client;

    @Inject
    public Lognit(RestClient client) {
        this.client = client;
    }

    public Welcome login(String server, String login, String password) throws IOException {
        client.authenticate(server, login, password);
        return info();
    }

    public Welcome info() throws IOException {
        return client.request(URL_WELCOME, Welcome.class);
    }

    public SearchChannel beginSearch(String query, SearchListener listener) throws IOException {
        return client.request(make(URL_SEARCH, encode(query)), SearchChannel.class);
    }

    public void logout() throws IOException {
        client.authenticate("", "", "");
    }

    private String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }
    
    private String make(String url, Object... args) {
        return String.format(url, args);
    }
}
