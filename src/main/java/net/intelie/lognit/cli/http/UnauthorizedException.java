package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.StatusLine;

public class UnauthorizedException extends RequestFailedException {
    public UnauthorizedException(StatusLine line) {
        super(line);
    }
}
