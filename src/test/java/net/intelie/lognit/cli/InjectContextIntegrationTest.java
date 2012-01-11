package net.intelie.lognit.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.intelie.lognit.cli.commands.Command;
import net.intelie.lognit.cli.commands.Info;
import net.intelie.lognit.cli.commands.Login;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class InjectContextIntegrationTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new MainModule());
    }

    @Test
    public void canCreateContext() {
        ArgsParser parser = injector.getInstance(ArgsParser.class);
        assertThat(parser).isInstanceOf(ArgsParser.class);
    }

    @Test
    public void hasAllCommands() {
        Command[] commands = injector.getInstance(Command[].class);
        assertThat(commands).hasAtLeastOneElementOfType(Login.class);
        assertThat(commands).hasAtLeastOneElementOfType(Info.class);
    }
}
