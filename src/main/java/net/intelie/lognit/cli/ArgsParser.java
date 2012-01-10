package net.intelie.lognit.cli;

import com.google.inject.Inject;
import net.intelie.lognit.cli.http.CookieStorage;
import net.intelie.lognit.cli.http.HttpWrapper;
import net.intelie.lognit.cli.model.Welcome;
import org.apache.commons.httpclient.HttpClient;

public class ArgsParser {
    private final HttpClient client;
    private final HttpWrapper wrapper;
    private final CookieStorage storage;

    @Inject
    public ArgsParser(HttpClient client, HttpWrapper wrapper, CookieStorage storage) {
        this.client = client;
        this.wrapper = wrapper;
        this.storage = storage;
    }

    public void run(String[] args) throws Exception {
        wrapper.authenticate(args[0], args[1]);
        Welcome welcome = wrapper.request("http://localhost:9006/rest/users/welcome", Welcome.class);
        System.out.println(welcome.getMessage());
    }
}
