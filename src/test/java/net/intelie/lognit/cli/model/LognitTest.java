package net.intelie.lognit.cli.model;

import net.intelie.lognit.cli.http.Entity;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestListener;
import net.intelie.lognit.cli.http.RestListenerHandle;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LognitTest {

    private RestClient client;
    private Lognit lognit;

    @Before
    public void setUp() throws Exception {
        client = mock(RestClient.class);
        lognit = new Lognit(client);
    }

    @Test
    public void testSetServer() throws Exception {
        lognit.setServer("abc");
        verify(client).setServer("abc");
    }

    @Test
    public void testGetServer() throws Exception {
        when(client.getServer()).thenReturn("abc");
        assertThat(lognit.getServer()).isEqualTo("abc");
        verify(client).getServer();
    }


    @Test
    public void testAuthenticate() throws Exception {
        lognit.authenticate("abc", "def");
        verify(client).authenticate("abc", "def");
    }

    @Test
    public void testWelcome() throws Exception {
        Welcome welcome = new Welcome("abc");
        when(client.get("/rest/users/welcome", Welcome.class)).thenReturn(welcome);
        assertThat(lognit.welcome()).isEqualTo(welcome);
    }

    @Test
    public void testPurge() throws Exception {
        Purge purge = new Purge("abc");
        Entity entity = new Entity().add("expression", "qwe").add("windowLength", "42");
        when(client.post("/rest/purge", entity, Purge.class)).thenReturn(purge);
        assertThat(lognit.purge("qwe", 42)).isEqualTo(purge);
    }

    @Test
    public void testStats() throws Exception {
        StatsSummary summary = mock(StatsSummary.class);
        when(client.get("/rest/stats", StatsSummary.class)).thenReturn(summary);
        assertThat(lognit.stats()).isEqualTo(summary);
    }

    @Test
    public void testTerms() throws Exception {
        Terms terms = new Terms(Arrays.asList("AAA", "BBB", "CCC"));
        when(client.get("/rest/terms?field=abc+qwe&term=123+456&avoidColons=true&size=100", Terms.class)).thenReturn(terms);
        assertThat(lognit.terms("abc qwe", "123 456")).isEqualTo(terms);
    }

    @Test
    public void testSearch() throws Exception {
        SearchChannel channel = new SearchChannel("lalala");
        RestListenerHandle handle = mock(RestListenerHandle.class);
        RestListener<MessageBag> listener = mock(RestListener.class);

        when(client.get("/rest/search?expression=qwe+asd&windowLength=20", SearchChannel.class)).thenReturn(channel);
        when(client.listen("lalala", MessageBag.class, listener)).thenReturn(handle);

        assertThat(lognit.search("qwe asd", 20, listener)).isEqualTo(handle);
    }
}
