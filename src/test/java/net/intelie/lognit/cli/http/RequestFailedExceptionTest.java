package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.StatusLine;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class RequestFailedExceptionTest {
    @Test
    public void theMessageWillBeTheStatusLine() throws Exception {
        RequestFailedException ex = new RequestFailedException(new StatusLine("HTTP/1.0 200 OK"));
        assertEquals("HTTP/1.0 200 OK", ex.getMessage());
    }
}
