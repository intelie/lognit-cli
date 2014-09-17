package net.intelie.lognit.cli.http;

import com.google.common.collect.Iterators;
import com.google.common.io.ByteStreams;
import net.intelie.lognit.cli.json.Jsonizer;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.cookie.RFC2965Spec;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.ext.AckExtension;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

public class RestClientImpl implements RestClient {
    private static final CookieSpec COOKIE_SPEC = new RFC2965Spec();
    private final HttpClient client;
    private final MethodFactory methods;
    private final BayeuxFactory bayeux;
    private final Jsonizer jsonizer;

    private String server;
    private boolean authenticated;

    public RestClientImpl(HttpClient client, MethodFactory methods, BayeuxFactory bayeux, Jsonizer jsonizer) throws Exception {
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
        client.getState().clearCookies();
        setServer(state.getServer());
        client.getState().addCookies(state.getCookies());
    }

    public String getServer() {
        return server;
    }

    @Override
    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public void authenticate(String username, String password) {
        client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        client.getParams().setAuthenticationPreemptive(true);
        this.authenticated = true;
    }

    @Override
    public <T> T get(String uri, Class<T> type) throws IOException {
        GetMethod method = methods.get(prependServer(uri));
        execute(method);
        return deserializeBody(method, type);
    }

    @Override
    public <T> RestStream<T> getStream(String uri, Class<T> type) throws IOException {
        GetMethod method = methods.get(prependServer(uri));
        execute(method);
        return deserializeBodyStream(method, type);
    }

    @Override
    public <T> T post(String uri, Entity entity, Class<T> type) throws IOException {
        PostMethod method = methods.post(prependServer(uri));
        entity.executeOn(method);
        execute(method);
        return deserializeBody(method, type);
    }

    private <T> RestStream<T> deserializeBodyStream(HttpMethod method, Class<T> type) throws IOException {
        InputStream stream = method.getResponseBodyAsStream();
        if (stream == null)
            return new RestStream<T>(Iterators.<T>emptyIterator(), null);
        return new RestStream<T>(jsonizer.from(stream, type), stream);
    }

    private <T> T deserializeBody(HttpMethod method, Class<T> type) throws IOException {
        InputStream stream = method.getResponseBodyAsStream();
        if (stream == null)
            return null;
        String body = new String(ByteStreams.toByteArray(stream));
        return jsonizer.from(body, type);
    }

    @Override
    public <T> RestListenerHandle listen(String channel, final Class<T> type, final RestListener<T> listener) throws IOException {
        String url = prependServer("cometd");
        BayeuxClient cometd = bayeux.create(url);
        cometd.addExtension(new AckExtension());

        Cookie[] cookies = getMatchingCookies(url);
        for (Cookie cookie : cookies)
            cometd.setCookie(cookie.getName(), cookie.getValue());

        cometd.handshake(120000);

        final BayeuxHandle handle = new BayeuxHandle(cometd);
        cometd.getChannel("/meta/connect").addListener(new ClientSessionChannel.MessageListener() {
            @Override
            public void onMessage(ClientSessionChannel channel, Message message) {
                if (!message.isSuccessful())
                    handle.invalidate();
            }
        });

        cometd.getChannel(channel).subscribe(new JsonMessageListener<T>(listener, type, jsonizer));
        return handle;
    }

    private Cookie[] getMatchingCookies(String url) {
        URI uri = URI.create(url);
        Cookie[] cookies = client.getState().getCookies();
        if (cookies == null) return new Cookie[0];
        return COOKIE_SPEC.match(uri.getHost(),
                httpPort(uri),
                uri.getPath(),
                isHttps(uri),
                cookies);
    }

    private boolean isHttps(URI uri) {
        return "https".equalsIgnoreCase(uri.getScheme());
    }

    private int httpPort(URI uri) {
        int port = uri.getPort();
        if (port == -1)
            return isHttps(uri) ? 443 : 80;
        return port;
    }

    private String prependServer(String uri) throws MalformedURLException {
        String safeUri = uri.startsWith("/") ? uri : "/" + uri;
        String safeServer = server.startsWith("http://") || server.startsWith("https://") ? server : "http://" + server;
        uri = String.format("%s%s", safeServer, safeUri);

        return uri;
    }

    private void execute(HttpMethod method) throws IOException {
        method.getParams().setCookiePolicy(CookiePolicy.DEFAULT);

        method.setDoAuthentication(authenticated);

        int response = client.executeMethod(method);

        if (response >= 300 && response < 500)
            throw new UnauthorizedException(method.getStatusLine());

        if (response < 200 || response >= 300)
            throw new RequestFailedException(method.getStatusLine());
    }

}
