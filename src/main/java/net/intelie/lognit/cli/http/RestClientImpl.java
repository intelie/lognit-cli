package net.intelie.lognit.cli.http;

import com.google.inject.Inject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.io.IOUtils;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;

public class RestClientImpl implements RestClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HttpClient client;
    private final MethodFactory methods;
    private final BayeuxFactory bayeux;
    private final Jsonizer jsonizer;

    private String server;
    private boolean authenticated;

    @Inject
    public RestClientImpl(HttpClient client, MethodFactory methods, BayeuxFactory bayeux, Jsonizer jsonizer) {
        this.client = client;
        this.methods = methods;
        this.bayeux = bayeux;
        this.jsonizer = jsonizer;
        this.server = "localhost";
        this.authenticated = false;
    }

    @Override
    public RestState getState() {
        return new RestState(client.getState().getCookies(), server);
    }

    @Override
    public void setState(RestState state) {
        client.getState().addCookies(state.getCookies());
        server = state.getServer();
    }

    @Override
    public void authenticate(String server, String username, String password) throws MalformedURLException {
        client.getState().clearCookies();
        client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        client.getParams().setAuthenticationPreemptive(true);

        this.server = server;
        this.authenticated = true;
    }

    @Override
    public <T> T request(String uri, Class<T> responseClass) throws IOException {
        uri = prependServer(uri);

        HttpMethod method = execute(uri);

        String body = IOUtils.toString(method.getResponseBodyAsStream());
        return jsonizer.from(body, responseClass);
    }

    @Override
    public <T> void listen(String channel, final Class<T> type, final RestListener<T> listener) throws IOException {
        String url = prependServer("cometd");
        System.out.println(url);
        BayeuxClient cometd = bayeux.create(url, client.getState().getCookies());
        cometd.handshake();
        cometd.getChannel(channel).subscribe(new ClientSessionChannel.MessageListener() {
            @Override
            public void onMessage(ClientSessionChannel clientSessionChannel, Message message) {
                listener.receive(jsonizer.from((String) message.getData(), type));
            }
        });
    }

    private String prependServer(String uri) throws MalformedURLException {
        System.out.printf("server: %s\n", server);
        String safeUri = uri.startsWith("/") ? uri : "/" + uri;
        uri = String.format("http://%s%s", server, safeUri);

        return uri;
    }

    private HttpMethod execute(String uri) throws IOException {
        HttpMethod method = methods.get(uri);

        method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

        method.setDoAuthentication(authenticated);
        if (client.executeMethod(method) != 200)
            throw new RequestFailedException(method.getStatusLine());
        return method;
    }

}
