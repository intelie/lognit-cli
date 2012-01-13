package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.Lognit;
import net.intelie.lognit.cli.model.Welcome;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LoginCommandTest {
    @Test
    public void nameIsLogin() {
        assertThat(new LoginCommand(null, null).name()).isEqualTo("login");
    }

    @Test
    public void willCallLognitLogin() throws Exception {
        Lognit lognit = mock(Lognit.class);
        UserInput input = mock(UserInput.class);

        when(input.readLine(anyString())).thenReturn("B");
        when(input.readPassword(anyString())).thenReturn("C");
        when(lognit.login("A", "B", "C")).thenReturn(new Welcome("abc"));

        LoginCommand info =  new LoginCommand(input, lognit);
        info.execute(new ArgsParser("-s", "A"));

        verify(input).println("abc");
    }

    @Test
    public void willCallLognitLoginWithoutAskingUser() throws Exception {
        Lognit lognit = mock(Lognit.class);
        UserInput input = mock(UserInput.class);

        when(input.readPassword(anyString())).thenReturn("C");
        when(lognit.login("A", "B", "C")).thenReturn(new Welcome("abc"));

        LoginCommand info =  new LoginCommand(input, lognit);
        info.execute(new ArgsParser("-s", "A", "-u", "B"));

        verify(input).println("abc");
    }

}
