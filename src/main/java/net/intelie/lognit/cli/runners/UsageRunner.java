package net.intelie.lognit.cli.runners;

import com.google.common.io.Resources;
import net.intelie.lognit.cli.Runner;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;

import java.nio.charset.Charset;

public class UsageRunner implements Runner {
    private final UserConsole console;

    public UsageRunner(UserConsole console) {
        this.console = console;
    }

    @Override
    public int run(UserOptions options) {
        try {
            console.println(Resources.toString(Resources.getResource("usage.txt"), Charset.defaultCharset()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
