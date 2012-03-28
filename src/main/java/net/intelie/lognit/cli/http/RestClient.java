package net.intelie.lognit.cli.http;

import java.io.IOException;

public interface RestClient {
    RestState getState();

    void setState(RestState state);

    String getServer();

    void setServer(String server);

    void authenticate(String username, String password);

    <T> T get(String uri, Class<T> type) throws IOException;

    <T> RestStream<T> getStream(String uri, Class<T> type) throws IOException;

    <T> T post(String uri, Entity entity, Class<T> type) throws IOException;
    
    <T> RestListenerHandle listen(String uri, Class<T> type, RestListener<T> listener) throws IOException;
}
