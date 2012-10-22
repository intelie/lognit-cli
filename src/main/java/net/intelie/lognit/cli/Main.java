package net.intelie.lognit.cli;

public class Main {
    public static void main(String... args) throws Exception {
        System.exit(new AppContext().resolveEntryPoint().run(args));
    }

}
