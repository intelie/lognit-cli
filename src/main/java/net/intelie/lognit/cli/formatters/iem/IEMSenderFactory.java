package net.intelie.lognit.cli.formatters.iem;

import com.google.common.base.CharMatcher;
import net.intelie.lognit.cli.formatters.Formatter;
import net.intelie.lognit.cli.json.Jsonizer;
import net.ser1.stomp.Client;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URI;

public class IEMSenderFactory {
    public static final int DEFAULT_PORT = 61613;
    private final Formatter console;
    private final Jsonizer jsonizer;
    private final StompClientFactory clientFactory;

    public IEMSenderFactory(Formatter console, Jsonizer jsonizer, StompClientFactory clientFactory) {
        this.console = console;
        this.jsonizer = jsonizer;
        this.clientFactory = clientFactory;
    }

    public IEMSender create(String definition) throws Exception {
        URI uri = URI.create(definition);
        String eventType = CharMatcher.is('/').trimFrom(uri.getPath());
        return new IEMSender(console, createClient(uri), jsonizer, eventType);


    }

    private Client createClient(URI uri) throws IOException, LoginException {
        String user = null, pass = null;
        String uriUserInfo = uri.getUserInfo();
        if (uriUserInfo != null) {
            String[] userInfo = uriUserInfo.split(":", 2);
            user = userInfo[0];
            pass = userInfo.length > 1 ? userInfo[1] : null;
        }

        int port = uri.getPort();
        if (port == -1)
            port = DEFAULT_PORT;

        return clientFactory.create(uri.getHost(), port, user, pass);
    }
}
