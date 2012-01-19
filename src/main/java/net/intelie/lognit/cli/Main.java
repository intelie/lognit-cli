package net.intelie.lognit.cli;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.intelie.lognit.cli.http.Jsonizer;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestClientImpl;
import net.intelie.lognit.cli.input.EntryPoint;
import net.intelie.lognit.cli.input.UsageRunner;
import net.intelie.lognit.cli.input.UserOptions;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.state.RestStateStorage;

import java.io.File;

public class Main extends AbstractModule {
    @Override
    protected void configure() {
        bind(RestClient.class).to(RestClientImpl.class).in(Singleton.class);
    }

    @Provides
    private RestStateStorage storage(Jsonizer jsonizer) {
        return new RestStateStorage(new File(new File(System.getProperty("user.home"), ".lognit"), "state"), jsonizer);
    }

    public static void main(String... args) {
        Guice.createInjector(new Main())
                .getInstance(EntryPoint.class)
                .run(args);
    }
}
