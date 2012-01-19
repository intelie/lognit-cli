package net.intelie.lognit.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class MainTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new Main());
    }

    @Test
    public void callingWithoutParamsWontBreakTheWorld()  {
        Main.main("--help");
    }
}
