package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Welcome;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class WelcomeRunnerTest {
    private UserConsole console;
    private Lognit lognit;
    private WelcomeRunner runner;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        lognit = mock(Lognit.class, RETURNS_DEEP_STUBS);
        runner = new WelcomeRunner(console, lognit);
    }

    @Test
    public void whenRunning() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        assertThat(runner.run(new UserOptions())).isEqualTo(0);
        verify(lognit).getServer();
        verify(lognit).welcome();
        verify(console).println("(%s): %s", null, "blablabla");
        verifyNoMoreInteractions(console, lognit);
    }

}
