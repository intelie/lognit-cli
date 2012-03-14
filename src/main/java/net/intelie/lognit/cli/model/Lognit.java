package net.intelie.lognit.cli.model;

import net.intelie.lognit.cli.http.Entity;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestListener;
import net.intelie.lognit.cli.http.RestListenerHandle;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Lognit {
    public static final String URL_WELCOME = "/rest/users/welcome";
    public static final String URL_PURGE = "/rest/purge";
    public static final String URL_UNPURGE = "/rest/purge/unpurge";
    public static final String URL_PURGE_INFO = "/rest/purge/%s";
    public static final String URL_PURGE_CANCEL = "/rest/purge/cancel/%s";
    public static final String URL_STATS = "/rest/stats";
    public static final String URL_SEARCH = "/rest/search?expression=%s&windowLength=%d";
    public static final String URL_TERMS = "/rest/terms?field=%s&term=%s&avoidColons=true&size=100";
    private final RestClient client;

    public Lognit(RestClient client) {
        this.client = client;
    }

    public void setServer(String server) {
        client.setServer(server);
    }

    public String getServer() {
        return client.getServer();
    }

    public void authenticate(String username, String password) {
        client.authenticate(username, password);
    }

    public RestListenerHandle search(String query, int windowLength, RestListener<MessageBag> listener) throws IOException {
        SearchChannel channel = client.get(make(URL_SEARCH, encode(query), windowLength), SearchChannel.class);
        return client.listen(channel.getChannel(), MessageBag.class, listener);
    }

    public Purge purge(String query, int windowLength) throws IOException {
        Entity entity = new Entity()
                .add("expression", query)
                .add("windowLength", Integer.toString(windowLength));
        return client.post(URL_PURGE, entity, Purge.class);
    }

    public Purge unpurge() throws IOException {
        return client.post(URL_UNPURGE, new Entity(), Purge.class);
    }

    public PurgeInfo purgeInfo(String id) throws IOException {
        return client.get(make(URL_PURGE_INFO, id), PurgeInfo.class);
    }

    public void cancelPurge(String id) throws IOException {
        client.post(make(URL_PURGE_CANCEL, id), new Entity(), Void.class);
    }

    public Terms terms(String field, String term) throws IOException {
        return client.get(make(URL_TERMS, encode(field), encode(term)), Terms.class);
    }

    public Welcome welcome() throws IOException {
        return client.get(URL_WELCOME, Welcome.class);
    }

    public StatsSummary stats() throws IOException {
        return client.get(URL_STATS, StatsSummary.class);
    }

    private String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }

    private String make(String url, Object... args) {
        return String.format(url, args);
    }
}