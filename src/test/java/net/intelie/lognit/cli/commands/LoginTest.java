package net.intelie.lognit.cli.commands;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class LoginTest {
    @Test
    public void nameIsLogin() {
        assertThat(new Login(null, null).name()).isEqualTo("login");
    }
}
