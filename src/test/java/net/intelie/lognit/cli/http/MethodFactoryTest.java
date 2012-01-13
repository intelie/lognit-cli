package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class MethodFactoryTest {
    @Test
    public void whenCreatingGetMethod() throws Exception {
        MethodFactory factory = new MethodFactory();
        String uri = "http://test.com:8080/abc";
        HttpMethod method = factory.get(uri);
        
        assertThat(method.getURI()).isEqualTo(new URI(uri, false));
        assertThat(method.getName()).isEqualTo("GET");
        assertThat(method.getFollowRedirects()).isEqualTo(false);
        assertThat(method).isInstanceOf(GetMethod.class);
    }


}
