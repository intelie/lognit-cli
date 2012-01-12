package net.intelie.lognit.cli;

import com.google.inject.Guice;
import net.intelie.lognit.cli.input.ArgsParser;

public class Main {
    public static void main(String... args) {
        Guice.createInjector(new MainModule())
                .getInstance(ArgsParser.class)
                .run(args);
    }
}
