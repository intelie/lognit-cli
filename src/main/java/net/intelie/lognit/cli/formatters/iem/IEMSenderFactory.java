package net.intelie.lognit.cli.formatters.iem;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.json.Jsonizer;
import net.ser1.stomp.Client;
import org.apache.commons.lang.StringUtils;

import java.net.URI;

public class IEMSenderFactory {
    private final UserConsole console;
    private final Jsonizer jsonizer;

    public IEMSenderFactory(UserConsole console, Jsonizer jsonizer) {
        this.console = console;
        this.jsonizer = jsonizer;
    }

    public IEMSender create(String definition) throws Exception {
        URI uri = URI.create(definition);
        String user = null, pass = null;
        String uriUserInfo = uri.getUserInfo();
        if (uriUserInfo != null) {
            String[] userInfo = uriUserInfo.split(":", 2);
            user = userInfo[0];
            pass = userInfo[1];
        }
        String path = StringUtils.strip(uri.getPath(), "/");
        return new IEMSender(console, new Client(uri.getHost(), uri.getPort(), user, pass), jsonizer, path);

    }
}
