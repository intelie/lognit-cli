package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class MethodFactory {
    public HttpMethod get(String uri) {
        return new GetMethod(uri);
    }
}
