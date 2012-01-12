package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RestClientTest {
    private HttpClient client;
    private MethodFactory methodFactory;
    private Jsonizer jsonizer;
    private RestClient wrapper;

    @Before
    public void setUp() throws Exception {
        client = mock(HttpClient.class, RETURNS_DEEP_STUBS);
        methodFactory = mock(MethodFactory.class, RETURNS_DEEP_STUBS);
        jsonizer = mock(Jsonizer.class);
        wrapper = new RestClient(client, methodFactory, jsonizer);
    }

    @Test
    public void whenAuthenticating() {
        wrapper.authenticate("abc", "123");

        verify(client.getState()).clearCookies();
        verify(client.getParams()).setAuthenticationPreemptive(true);
        verify(client.getState()).setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("abc", "123"));
        verifyNoMoreInteractions(client.getParams(), client.getState());
    }

    @Test
    public void willExecuteSuccessfulRequest() throws Exception {
        HttpMethod method = mockReturn("abc", "HTTP/1.0 200 OK", String.class, "QWEQWE");

        assertThat(wrapper.request("abc", String.class)).isEqualTo("QWEQWE");

        verify(method, times(0)).getStatusLine();
        verify(method).setDoAuthentication(false);
        verify(client).executeMethod(methodFactory.get("abc"));
    }

    private ByteArrayInputStream getStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    @Test
    public void willExecuteSuccessfulRequestAuthenticating() throws Exception {
        HttpMethod method = mockReturn("abc", "HTTP/1.0 200 OK", String.class, "QWEQWE");

        wrapper.authenticate("abc", "qwe");
        assertThat(wrapper.request("abc", String.class)).isEqualTo("QWEQWE");

        verify(method, times(0)).getStatusLine();
        verify(method, times(1)).setDoAuthentication(true);
        verify(client).executeMethod(methodFactory.get("abc"));
    }

    @Test
    public void willUseCompatibilityToHandleCookies() throws Exception {
        HttpMethod method = mockReturn("abc", "HTTP/1.0 200 OK", String.class, "BLABLA");

        wrapper.request("abc", String.class);

        verify(method.getParams()).setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
    }

    @Test
    public void willThrowOnUnsuccessfulRequest() throws Exception {
        mockReturn("abc", "HTTP/1.0 401 OK", String.class, "BLABLA");

        try {
            wrapper.request("abc", String.class);
            fail("should throw");
        } catch (RequestFailedException ex) {
            assertThat(ex.getMessage()).isEqualTo("HTTP/1.0 401 OK");
        }
    }

    private <T> HttpMethod mockReturn(String url, String line, Class<T> type, T object) throws IOException {
        HttpMethod method = methodFactory.get(url);
        when(method.getResponseBodyAsStream()).thenReturn(getStream("BLABLA".getBytes()));
        StatusLine statusLine = new StatusLine(line);
        when(method.getStatusLine()).thenReturn(statusLine);
        when(client.executeMethod(method)).thenReturn(statusLine.getStatusCode());
        when(jsonizer.from("BLABLA", type)).thenReturn(object);
        return method;
    }
}
