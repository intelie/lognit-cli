package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.StatusLine;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.Assertions.assertThat;

public class RequestFailedExceptionTest {
    @Test
    public void theMessageWillBeTheStatusLine() throws Exception {
        RequestFailedException ex = new RequestFailedException(new StatusLine("HTTP/1.0 200 OK"));
        assertThat(ex.getMessage()).isEqualTo("HTTP/1.0 200 OK");
}
}
