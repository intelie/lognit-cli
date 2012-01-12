package net.intelie.lognit.cli.input;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class LoginTest {
    @Test
    public void nameIsLogin() {
        assertThat(new LoginCommand(null, null).name()).isEqualTo("login");
    }
}
