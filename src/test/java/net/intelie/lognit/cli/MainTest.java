package net.intelie.lognit.cli;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class MainTest {
    @Test
    public void callingWithoutParamsWontBreakTheWorld() throws Exception {
        Main.resolveEntryPoint().run();
    }
}
