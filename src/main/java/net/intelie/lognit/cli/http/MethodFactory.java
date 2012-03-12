package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class MethodFactory {
    public GetMethod get(String uri) {
        GetMethod method = new GetMethod(uri);
        method.setFollowRedirects(false);
        return method;
    }

    public PostMethod post(String uri) {
        PostMethod method = new PostMethod(uri);
        method.setFollowRedirects(false);
        return method;
    }
}
