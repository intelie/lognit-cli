package net.intelie.lognit.cli;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.intelie.lognit.cli.state.CookieStorage;
import org.apache.commons.httpclient.HttpClient;

import java.io.File;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HttpClient.class).in(Singleton.class);
        bind(CookieStorage.class).toInstance(
                new CookieStorage(new File(System.getProperty("user.home"), ".lognit/cookies")));

    }
}
