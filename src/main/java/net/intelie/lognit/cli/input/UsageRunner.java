package net.intelie.lognit.cli.input;

import com.google.common.io.Resources;
import com.google.inject.Inject;

import java.nio.charset.Charset;

public class UsageRunner {
    private final UserConsole console;

    @Inject
     public UsageRunner(UserConsole console) {
        this.console = console;
    }

    public int run() {
        try {
            console.println(Resources.toString(Resources.getResource("usage.txt"), Charset.defaultCharset()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
