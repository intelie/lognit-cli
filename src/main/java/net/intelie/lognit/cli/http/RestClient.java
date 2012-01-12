package net.intelie.lognit.cli.http;

import java.io.IOException;
import java.net.MalformedURLException;

public interface RestClient {
    RestState getState();

    void setState(RestState state);

    void authenticate(String server, String username, String password) throws MalformedURLException;

    <T> T request(String uri, Class<T> responseClass) throws IOException;
}
