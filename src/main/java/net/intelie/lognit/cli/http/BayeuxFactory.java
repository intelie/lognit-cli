package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.Cookie;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;

import java.util.HashMap;

public class BayeuxFactory {
    public BayeuxClient create(String uri, Cookie[] cookies) {
        BayeuxClient client = new BayeuxClient(uri, LongPollingTransport.create(new HashMap<String, Object>()));
        for (Cookie cookie : cookies)
            client.setCookie(cookie.getName(), cookie.getValue());
        return client;
    }
}
