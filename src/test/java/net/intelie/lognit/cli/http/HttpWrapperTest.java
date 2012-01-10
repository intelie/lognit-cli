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

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HttpWrapperTest {
    private HttpClient client;
    private MethodFactory methodFactory;
    private Jsonizer jsonizer;
    private HttpWrapper wrapper;

    @Before
    public void setUp() throws Exception {
        client = mock(HttpClient.class, RETURNS_DEEP_STUBS);
        methodFactory = mock(MethodFactory.class, RETURNS_DEEP_STUBS);
        jsonizer = mock(Jsonizer.class);
        wrapper = new HttpWrapper(client, methodFactory, jsonizer);
    }

    @Test
    public void whenAuthenticating() {
        wrapper.authenticate("abc", "123");

        verify(client.getParams()).setAuthenticationPreemptive(true);
        verify(client.getState()).setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("abc", "123"));
        verifyNoMoreInteractions(client.getParams(), client.getState());
    }

    @Test
    public void willExecuteSuccessfulRequest() throws Exception {
        HttpMethod method = methodFactory.get("abc");

        when(method.getResponseBodyAsStream()).thenReturn(getStream("BLABLA"));
        when(client.executeMethod(method)).thenReturn(200);
        when(jsonizer.from("BLABLA", String.class)).thenReturn("QWEQWE");

        String result = wrapper.request("abc", String.class);

        assertThat(result).isEqualTo("QWEQWE");

        verify(method, times(0)).getStatusLine();
        verify(method).setDoAuthentication(false);
        verify(client).executeMethod(methodFactory.get("abc"));
    }

    private ByteArrayInputStream getStream(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }

    @Test
    public void willExecuteSuccessfulRequestAuthenticating() throws Exception {
        HttpMethod method = methodFactory.get("abc");

        when(method.getResponseBodyAsStream()).thenReturn(getStream("BLABLA"));
        when(client.executeMethod(method)).thenReturn(200);
        when(jsonizer.from("BLABLA", String.class)).thenReturn("QWEQWE");

        wrapper.authenticate("abc", "qwe");
        String result = wrapper.request("abc", String.class);

        assertThat(result).isEqualTo("QWEQWE");

        verify(method, times(0)).getStatusLine();
        verify(method, times(1)).setDoAuthentication(true);
        verify(client).executeMethod(methodFactory.get("abc"));
    }

    @Test
    public void willUseCompatibilityToHandleCookies() throws Exception {
        HttpMethod method = methodFactory.get("abc");

        when(method.getResponseBodyAsStream()).thenReturn(getStream("BLABLA"));
        when(client.executeMethod(method)).thenReturn(200);
        when(jsonizer.from("BLABLA", String.class)).thenReturn("QWEQWE");

        wrapper.request("abc", String.class);

        verify(method.getParams()).setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
    }

    @Test
    public void willUseNoCookiesNoCookies() throws Exception {
        HttpMethod method = methodFactory.get("abc");

        when(method.getResponseBodyAsStream()).thenReturn(getStream("BLABLA"));
        when(client.executeMethod(method)).thenReturn(200);
        when(jsonizer.from("BLABLA", String.class)).thenReturn("QWEQWE");

        wrapper.requestNoCookies("abc", String.class);

        verify(method.getParams()).setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
    }

    @Test
    public void willThrowOnUnsuccessfulRequest() throws Exception {
        HttpMethod method = methodFactory.get("abc");

        when(method.getResponseBodyAsStream()).thenReturn(getStream("BLABLA"));
        when(method.getStatusLine()).thenReturn(new StatusLine("HTTP/1.0 401 OK"));
        when(client.executeMethod(method)).thenReturn(401);
        when(jsonizer.from("BLABLA", String.class)).thenReturn("QWEQWE");

        try {
            wrapper.request("abc", String.class);
            fail("should throw");
        } catch (RequestFailedException ex) {
            assertThat(ex.getMessage()).isEqualTo("HTTP/1.0 401 OK");
        }
    }
}
