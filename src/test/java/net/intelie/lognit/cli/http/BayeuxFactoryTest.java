package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.Cookie;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.client.BayeuxClient;
import org.junit.Test;

public class BayeuxFactoryTest {
    @Test
    public void testCreate() throws Exception {
        BayeuxFactory factory = new BayeuxFactory();
        BayeuxClient session = (BayeuxClient)factory.create("http://localhost/cometd", new Cookie[0]);
    }
}
