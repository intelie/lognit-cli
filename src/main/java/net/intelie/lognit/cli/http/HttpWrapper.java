package net.intelie.lognit.cli.http;

import com.google.gson.Gson;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

import java.io.IOException;

public class HttpWrapper {
    private final HttpClient client;
    private final MethodFactory methods;
    private final Gson gson;

    public HttpWrapper(HttpClient client, MethodFactory methods, Gson gson) {
        this.client = client;
        this.methods = methods;
        this.gson = gson;
    }

    public void authenticate(String username, String password) {
        client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        client.getParams().setAuthenticationPreemptive(true);
    }

    public <T> T request(String uri, Class<T> responseClass) throws IOException {
        HttpMethod method = execute(uri);
        checkReturn(method);
        return gson.fromJson(method.getResponseBodyAsString(), responseClass);
    }

    private void checkReturn(HttpMethod method) throws RequestFailedException {
        StatusLine line = method.getStatusLine();
        if (line.getStatusCode() != 200) throw new RequestFailedException(line);
    }

    private HttpMethod execute(String uri) throws IOException {
        HttpMethod method = methods.get(uri);
        method.setDoAuthentication(true);
        client.executeMethod(method);
        return method;
    }
}
