package net.intelie.lognit.cli.formatters.iem;

import net.ser1.stomp.Client;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class StompClientFactory {
    public Client create(String server, int port, String user, String password) throws IOException, LoginException {
        //for testability
        return new Client(server, port, user, password);
    }
}
