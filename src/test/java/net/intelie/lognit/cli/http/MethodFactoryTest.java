package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MethodFactoryTest {
    @Test
    public void whenCreatingGetMethod() throws Exception {
        MethodFactory factory = new MethodFactory();
        String uri = "http://test.com:8080/abc";
        HttpMethod method = factory.get(uri);
        
        assertEquals(new URI(uri, false), method.getURI());
        assertEquals("GET", method.getName());
        assertEquals(GetMethod.class, method.getClass());
    }
}
