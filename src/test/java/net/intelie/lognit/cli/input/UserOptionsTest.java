package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.model.Lognit;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserOptionsTest {

    private UsagePrinter usage;
    private Lognit lognit;

    @Before
    public void setUp() throws Exception {
        usage = mock(UsagePrinter.class);
        lognit = mock(Lognit.class);
    }

    @Test
    public void whenParsingHelp() throws Exception {
        UserOptions options = new UserOptions(usage, lognit, "-?");
        options.run();
        verify(usage).run();
    }

    @Test
    public void whenParsingServer() throws Exception {
        UserOptions options = new UserOptions(usage, lognit, "-s", "test");
        options.run();
        verify(lognit).setServer("test");
    }

}
