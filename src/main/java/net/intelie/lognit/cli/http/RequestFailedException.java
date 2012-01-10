package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.StatusLine;

import java.io.IOException;

public class RequestFailedException extends IOException {
    private final StatusLine line;

    public RequestFailedException(StatusLine line) {
        super(line.toString());
        this.line = line;
    }
}
