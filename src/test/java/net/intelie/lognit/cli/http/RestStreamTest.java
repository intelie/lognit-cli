package net.intelie.lognit.cli.http;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RestStreamTest {
    @Test
    public void testIterate() throws Exception {
        LinkedList<String> list = new LinkedList<String>(Arrays.asList("AAA", "BBB", "CCC"));
        RestStream<String> stream = new RestStream<String>(list.iterator(), null);
        assertThat(stream.next()).isEqualTo("AAA");
        stream.remove();
        assertThat(list.size()).isEqualTo(2);

        assertThat(stream.next()).isEqualTo("BBB");
        stream.remove();
        assertThat(list.size()).isEqualTo(1);

        assertThat(stream.next()).isEqualTo("CCC");
        stream.remove();
        assertThat(list.size()).isEqualTo(0);
        assertThat(stream.hasNext()).isFalse();
    }

    @Test
    public void testClose() throws Exception {
        InputStream in = mock(InputStream.class);
        RestStream<String> stream = new RestStream<String>(null, in);
        stream.close();
        verify(in).close();
    }

    @Test
    public void testCloseNullStream() throws Exception {
        RestStream<String> stream = new RestStream<String>(null, null);
        stream.close();
    }


    @Test
    public void testCloseExceptionWontBlow() throws Exception {
        InputStream in = mock(InputStream.class);
        doThrow(new IOException()).when(in).close();
        RestStream<String> stream = new RestStream<String>(null, in);
        stream.close();
        verify(in).close();
    }
}
