package net.intelie.lognit.cli;

import com.google.inject.*;
import net.intelie.lognit.cli.input.Command;
import net.intelie.lognit.cli.input.InfoCommand;
import net.intelie.lognit.cli.input.LoginCommand;
import net.intelie.lognit.cli.http.Jsonizer;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestClientImpl;
import net.intelie.lognit.cli.state.RestStateStorage;

import java.io.File;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RestClient.class).to(RestClientImpl.class).in(Singleton.class);
    }

    @Provides
    private Command[] commands(LoginCommand login, InfoCommand info) {
        return new Command[] { login, info };
    }

    @Provides
    private RestStateStorage restStateStorage(Jsonizer jsonizer) {
        return new RestStateStorage(
                new File(System.getProperty("user.home"), ".lognit/state"), jsonizer);
    }
}
