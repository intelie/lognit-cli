package net.intelie.lognit.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.intelie.lognit.cli.input.Command;
import net.intelie.lognit.cli.input.InfoCommand;
import net.intelie.lognit.cli.input.LoginCommand;
import org.junit.Before;
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
        Main.main();
    }

    @Test
    public void hasAllCommands() {
        Command[] commands = injector.getInstance(Command[].class);
        assertThat(commands).hasAtLeastOneElementOfType(LoginCommand.class);
        assertThat(commands).hasAtLeastOneElementOfType(InfoCommand.class);
    }
}
