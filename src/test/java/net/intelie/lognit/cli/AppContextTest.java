package net.intelie.lognit.cli;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class AppContextTest {
    @Test
    public void callingWithoutParamsWontBreakTheWorld() throws Exception {
        new AppContext().resolveEntryPoint().run("--help");
    }
}
