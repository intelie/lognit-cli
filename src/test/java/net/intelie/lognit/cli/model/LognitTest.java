package net.intelie.lognit.cli.model;

import net.intelie.lognit.cli.http.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LognitTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

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
        when(client.get("/rest/me/welcome", Welcome.class)).thenReturn(welcome);
        assertThat(lognit.welcome()).isEqualTo(welcome);
    }

    @Test
    public void testPurgeInfo() throws Exception {
        PurgeInfo info = mock(PurgeInfo.class);
        when(client.get("/rest/purge/abc?all=true", PurgeInfo.class)).thenReturn(info);
        assertThat(lognit.purgeInfo("abc", true)).isEqualTo(info);
    }

    @Test
    public void testPurge() throws Exception {
        Purge purge = new Purge("abc");
        Entity entity = new Entity().add("expression", "qwe").add("windowLength", "42").add("all", "false");
        when(client.post("/rest/purge", entity, Purge.class)).thenReturn(purge);
        assertThat(lognit.purge("qwe", 42, false)).isEqualTo(purge);
    }

    @Test
    public void testUnPurge() throws Exception {
        Purge purge = new Purge("abc");
        Entity entity = new Entity().add("all", "true");
        when(client.post("/rest/purge/unpurge", entity, Purge.class)).thenReturn(purge);
        assertThat(lognit.unpurge(true)).isEqualTo(purge);
    }

    @Test
    public void cancelPurge() throws Exception {
        lognit.cancelPurge("abc", false);
        Entity entity = new Entity().add("all", "false");
        verify(client).post("/rest/purge/cancel/abc", entity, Void.class);
    }

    @Test
    public void cancelAllPurges() throws Exception {
        Entity entity = new Entity().add("all", "true");
        lognit.cancelAllPurges(true);
        verify(client).post("/rest/purge/cancel-all", entity, Void.class);
    }

    @Test
    public void testPause() throws Exception {
        Pause pause = new Pause(42);
        Entity entity = new Entity().add("all", "true");
        when(client.post("/rest/pause", entity, Pause.class)).thenReturn(pause);
        assertThat(lognit.pause(true)).isEqualTo(pause);
    }

    @Test
    public void testResume() throws Exception {
        Pause pause = new Pause(42);
        Entity entity = new Entity().add("all", "false");
        when(client.post("/rest/pause/resume", entity, Pause.class)).thenReturn(pause);
        assertThat(lognit.resume(false)).isEqualTo(pause);
    }

    @Test
    public void testStats() throws Exception {
        StatsSummary summary = mock(StatsSummary.class);
        when(client.get("/rest/stats?timeout=123", StatsSummary.class)).thenReturn(summary);
        assertThat(lognit.stats(123)).isEqualTo(summary);
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

        when(client.get("/rest/search?expression=qwe+asd&windowLength=20&realtime=false&stats=true&span=what", SearchChannel.class)).thenReturn(channel);
        when(client.listen("lalala", MessageBag.class, listener)).thenReturn(handle);

        assertThat(lognit.search("qwe asd", 20, false, true, "what", listener)).isEqualTo(handle);
    }

    @Test
    public void testSearchWithError() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("aaaa");

        SearchChannel channel = new SearchChannel(null, new QueryInfo(false, "aaaa"));
        RestListener<MessageBag> listener = mock(RestListener.class);

        when(client.get("/rest/search?expression=qwe+asd&windowLength=20&realtime=false&stats=true&span=what", SearchChannel.class)).thenReturn(channel);

        lognit.search("qwe asd", 20, false, true, "what", listener);
    }

    @Test
    public void testDownload() throws Exception {
        RestStream<DownloadBag> stream = mock(RestStream.class);
        when(client.getStream("/rest/search/download?expression=qwe+asd&windowLength=20&span=what", DownloadBag.class)).thenReturn(stream);
        assertThat((Object) lognit.download("qwe asd", 20, "what")).isEqualTo(stream);
    }
}
