package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.Lognit;
import net.intelie.lognit.cli.model.Welcome;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LogoutCommandTest {
    @Test
    public void nameIsLogout() {
        assertThat(new LogoutCommand(null, null).name()).isEqualTo("logout");
    }

    @Test
    public void willCallLognitInfo() throws Exception {
        Lognit lognit = mock(Lognit.class);
        UserInput input = mock(UserInput.class);

        LogoutCommand logout = new LogoutCommand(input, lognit);
        logout.execute(new ArgsParser());

        verify(lognit).logout();
    }

}
