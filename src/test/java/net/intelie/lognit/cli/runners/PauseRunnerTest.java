package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Pause;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PauseRunnerTest {

    private Lognit lognit;
    private UserConsole console;
    private PauseRunner runner;

    @Before
    public void setUp() throws Exception {
        lognit = mock(Lognit.class);
        console = mock(UserConsole.class);
        runner = new PauseRunner(console, lognit);
    }

    @Test
    public void testPause() throws Exception {
        when(lognit.pause(true)).thenReturn(new Pause(42));
        assertThat(runner.run(new UserOptions("--pause", "--all"))).isEqualTo(0);
        
        verify(console).println(PauseRunner.PAUSED, 42);
    }

    @Test
    public void testResumeIsDefault() throws Exception {
        when(lognit.resume(false)).thenReturn(new Pause(42));

        assertThat(runner.run(new UserOptions())).isEqualTo(0);
        verify(console).println(PauseRunner.RESUMED, 42);
    }

}
