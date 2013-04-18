package net.intelie.lognit.cli.formatters.iem;

import net.intelie.gozirra.Client;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class StompClientFactory {
    public Client create(String server, int port, boolean ssl, String user, String password) throws IOException, LoginException {
        //for testability
        return new Client(server, port, ssl, user, password);
    }
}
