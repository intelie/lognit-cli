package net.intelie.lognit.cli;

import java.util.Arrays;

public class Main {
    public static void main(String... args) throws Exception {
        System.exit(new AppContext().resolveEntryPoint().run(args));
    }

}
