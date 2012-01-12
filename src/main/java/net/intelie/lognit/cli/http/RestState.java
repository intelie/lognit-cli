package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.Cookie;

import java.io.Serializable;

public class RestState implements Serializable {
    private final Cookie[] cookies;
    private final String server;

    public RestState(Cookie[] cookies, String server) {
        this.cookies = cookies;
        this.server = server;
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    public String getServer() {
        return server;
    }
}
