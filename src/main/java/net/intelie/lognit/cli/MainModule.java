package net.intelie.lognit.cli;

import com.google.inject.*;
import net.intelie.lognit.cli.commands.Command;
import net.intelie.lognit.cli.commands.Info;
import net.intelie.lognit.cli.commands.Login;
import net.intelie.lognit.cli.http.Jsonizer;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestClientImpl;
import net.intelie.lognit.cli.state.RestStateStorage;
import org.apache.commons.httpclient.HttpClient;

import java.io.File;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RestClient.class).to(RestClientImpl.class).in(Singleton.class);
    }

    @Provides
    private Command[] commands(Login login, Info info) {
        return new Command[] { login, info };
    }

    @Provides
    private RestStateStorage restStateStorage(Jsonizer jsonizer) {
        return new RestStateStorage(
                new File(System.getProperty("user.home"), ".lognit/cookies"), jsonizer);
    }
}
