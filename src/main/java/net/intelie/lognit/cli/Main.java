package net.intelie.lognit.cli;

import javax.net.ssl.SSLContext;

public class Main {
    public static void main(String... args) throws Exception {
        System.exit(new AppContext().resolveEntryPoint().run(args));
    }

}
