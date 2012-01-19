package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Welcome;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.fail;
import static org.mockito.Mockito.*;

public class RequestRunnerTest {
    private UserInput input;
    private Lognit lognit;
    private RequestRunner runner;

    @Before
    public void setUp() throws Exception {
        input = mock(UserInput.class);
        lognit = mock(Lognit.class);
        runner = new RequestRunner(input, lognit);
    }

    @Test
    public void whenHasNoQueryOnlyPrintsWelcome() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        runner.run(null, null, null, "");
        verify(lognit).welcome();
        verify(input).println("blablabla");
        verifyNoMoreInteractions(input, lognit);
    }

    @Test
    public void whenH asServerAlsoSetsTheServer() throws Exception {
        when(lognit.welcome()).thenReturn(new Welcome("blablabla"));
        runner.run("someserver", null, null, "");

        verify(lognit).setServer("someserver");
        verify(lognit).welcome();
        verify(input).println("blablabla");
        verifyNoMoreInteractions(input, lognit);
    }
}
