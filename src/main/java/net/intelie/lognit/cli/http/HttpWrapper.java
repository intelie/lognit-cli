package net.intelie.lognit.cli.http;

import com.google.inject.Inject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class HttpWrapper {
    private final HttpClient client;
    private final MethodFactory methods;
    private final Jsonizer jsonizer;
    private boolean authenticated;

    @Inject
    public HttpWrapper(HttpClient client, MethodFactory methods, Jsonizer jsonizer) {
        this.client = client;
        this.methods = methods;
        this.jsonizer = jsonizer;
        this.authenticated = false;
    }

    public void authenticate(String username, String password) {
        client.getState().clearCookies();
        client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        client.getParams().setAuthenticationPreemptive(true);
        authenticated = true;
    }

    public <T> T request(String uri, Class<T> responseClass) throws IOException {
        HttpMethod method = execute(uri);

        String body = IOUtils.toString(method.getResponseBodyAsStream());
        return jsonizer.from(body, responseClass);
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
