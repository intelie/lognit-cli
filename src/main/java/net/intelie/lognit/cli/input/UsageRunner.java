package net.intelie.lognit.cli.input;

import com.google.common.io.Resources;
import com.google.inject.Inject;

import java.nio.charset.Charset;

public class UsageRunner {
    private final UserInput console;

    @Inject
    public UsageRunner(UserInput console) {
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
