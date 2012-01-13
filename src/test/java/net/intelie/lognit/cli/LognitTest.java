package net.intelie.lognit.cli;

import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.model.SearchChannel;
import net.intelie.lognit.cli.model.Welcome;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LognitTest {

    private RestClient client;

    @Before
    public void setUp() throws Exception {
        client = mock(RestClient.class);
    }

    @Test
    public void testLogin() throws Exception {
        Welcome welcome = new Welcome("abc");

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

        when(client.request(Lognit.URL_WELCOME, Welcome.class)).thenReturn(welcome);

        Lognit lognit = new Lognit(client);
        assertThat(lognit.info()).isEqualTo(welcome);
    }

    @Test
    public void testLogout() throws Exception {
        Lognit lognit = new Lognit(client);
        lognit.logout();
        verify(client).authenticate("", "", "");
    }

    @Test
    public void testSearch() throws Exception {
        SearchChannel channel = new SearchChannel("abc");
        when(client.request("/rest/search?expression=qwe+qwe", SearchChannel.class)).thenReturn(channel);
        
        Lognit lognit = new Lognit(client);
        assertThat(lognit.beginSearch("qwe qwe", null)).isEqualTo(channel);
    }
}
