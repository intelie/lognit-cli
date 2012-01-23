package net.intelie.lognit.cli.http;

import java.io.IOException;
import java.net.MalformedURLException;

public interface RestClient {
    RestState getState();

    void setState(RestState state);

    void setServer(String server);
    
    void authenticate(String username, String password);

    <T> T request(String uri, Class<T> type) throws IOException;

    <T> RestListenerHandle listen(String uri, Class<T> type, RestListener<T> listener) throws IOException;
}
