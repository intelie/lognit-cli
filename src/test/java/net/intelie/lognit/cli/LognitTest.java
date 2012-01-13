package net.intelie.lognit.cli;

import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.model.Welcome;
import org.junit.Test;
import org.mockito.InOrder;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LognitTest {
    @Test
    public void testLogin() throws Exception {
        Welcome welcome = new Welcome("abc");
        RestClient client = mock(RestClient.class);

        when(client.request(Lognit.URL_WELCOME, Welcome.class)).thenReturn(welcome);

        Lognit lognit = new Lognit(client);
        assertThat(lognit.login("A", "B", "C")).isEqualTo(welcome);

        InOrder orderly = inOrder(client);
        orderly.verify(client).authenticate("A", "B", "C");
        orderly.verify(client).request(Lognit.URL_WELCOME, Welcome.class);
    }

    @Test
    public void testInfo() throws Exception {
        Welcome welcome = new Welcome("abc");
        RestClient client = mock(RestClient.class);

        when(client.request(Lognit.URL_WELCOME, Welcome.class)).thenReturn(welcome);

        Lognit lognit = new Lognit(client);
        assertThat(lognit.info()).isEqualTo(welcome);
    }

    @Test
    public void testLogout() throws Exception {
        RestClient client = mock(RestClient.class);
        Lognit lognit = new Lognit(client);
        lognit.logout();
        verify(client).authenticate("", "", "");
    }
}
