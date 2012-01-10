package net.intelie.lognit.cli;

import com.google.inject.AbstractModule;
import net.intelie.lognit.cli.http.CookieStorage;

import java.io.File;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CookieStorage.class).toInstance(
                new CookieStorage(new File(System.getProperty("user.home"), ".lognit/cookies")));

    }
}
