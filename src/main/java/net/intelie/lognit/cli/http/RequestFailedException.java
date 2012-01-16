package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.StatusLine;

import java.io.IOException;

public class RequestFailedException extends IOException {
    public RequestFailedException(StatusLine line) {
        super(line.toString());
    }
}
