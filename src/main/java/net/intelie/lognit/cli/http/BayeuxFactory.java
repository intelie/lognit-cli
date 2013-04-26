package net.intelie.lognit.cli.http;

import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;

public class BayeuxFactory {
    public BayeuxClient create(String uri) {
        HttpClient httpClient = new HttpClient();
        httpClient.setIdleTimeout(5000);
        httpClient.setConnectorType(HttpClient.CONNECTOR_SOCKET);
        httpClient.setMaxConnectionsPerAddress(32768);
        httpClient.setTimeout(5000);
        return new BayeuxClient(uri, LongPollingTransport.create(null, httpClient));
    }
}
