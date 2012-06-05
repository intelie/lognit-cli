package net.intelie.lognit.cli.http;

import com.google.common.base.Objects;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import net.intelie.lognit.cli.json.Jsonizer;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Arrays;

public class RestClientImpl implements RestClient {
    private final HttpClient client;
    private final MethodFactory methods;
    private final BayeuxFactory bayeux;
    private final Jsonizer jsonizer;

    private String server;
    private boolean authenticated;

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
        setServer(state.getServer());
        client.getState().addCookies(state.getCookies());
    }

    public String getServer() {
        return server;
    }

    @Override
    public void setServer(String server) {
        if (!Objects.equal(this.server, server))
            client.getState().clearCookies();
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
        String body = IOUtils.toString(stream);
        return jsonizer.from(body, type);
    }

    @Override
    public <T> RestListenerHandle listen(String channel, final Class<T> type, final RestListener<T> listener) throws IOException {
        String url = prependServer("cometd");
        BayeuxClient cometd = bayeux.create(url);

        Cookie[] cookies = client.getState().getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies)
                cometd.setCookie(cookie.getName(), cookie.getValue());


        cometd.handshake();
        cometd.waitFor(1000, BayeuxClient.State.CONNECTED);
        cometd.getChannel(channel).subscribe(new JsonMessageListener<T>(listener, type, jsonizer));
        return new BayeuxHandle(cometd);
    }

    private String prependServer(String uri) throws MalformedURLException {
        String safeUri = uri.startsWith("/") ? uri : "/" + uri;
        uri = String.format("http://%s%s", server, safeUri);

        return uri;
    }

    private void execute(HttpMethod method) throws IOException {
        method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

        method.setDoAuthentication(authenticated);

        int response = client.executeMethod(method);

        if (response >= 300 && response < 500)
            throw new UnauthorizedException(method.getStatusLine());

        if (response < 200 || response >= 300)
            throw new RequestFailedException(method.getStatusLine());
    }

}
