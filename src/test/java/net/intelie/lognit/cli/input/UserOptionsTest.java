package net.intelie.lognit.cli.input;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserOptionsTest {
    @Test
    public void whenParsingHelp() throws Exception {
        UsagePrinter usage = mock(UsagePrinter.class);
        UserOptions options = new UserOptions(usage, "-a", "-b", "-?");
        options.run();
        verify(usage).run();
    }


}
