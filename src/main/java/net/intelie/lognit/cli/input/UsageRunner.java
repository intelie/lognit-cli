package net.intelie.lognit.cli.input;

import com.google.common.io.Resources;

import java.nio.charset.Charset;

public class UsageRunner {
    private final UserConsole console;

    public UsageRunner(UserConsole console) {
        this.console = console;
    }

    public void run() {
        try {
            console.println(Resources.toString(Resources.getResource("usage.txt"), Charset.defaultCharset()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
