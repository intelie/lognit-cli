package net.intelie.lognit.cli.input;

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
        verify(request).run(new RequestOptions("test", null, null, "", null, null, false));
    }

    @Test
    public void whenParsingAllOptions() throws Exception {
        options.run("-s", "test", "-u", "user", "-p", "pass", "qqq", "-n", "42", "-t", "43", "-f");
        verify(request).run(new RequestOptions("test", "user", "pass", "qqq", 43, 42, true));
    }


}
