package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.model.Lognit;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class UserOptionsTest {

    private UsageRunner usage;
    private RequestRunner request;
    private UserOptions options;

    @Before
    public void setUp() throws Exception {
        usage = mock(UsageRunner.class);
        request = mock(RequestRunner.class);
        options = new UserOptions(usage, request);
    }

    @Test
    public void whenParsingHelp() throws Exception {
        options.run("-?", "-s", "test");
        verify(usage).run();
        verifyZeroInteractions(request);
    }

    @Test
    public void whenParsingServer() throws Exception {
        options.run("-s", "test");
        verify(request).run("test", null, null, "");
    }

}
