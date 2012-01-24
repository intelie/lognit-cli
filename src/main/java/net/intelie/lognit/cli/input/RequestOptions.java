package net.intelie.lognit.cli.input;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

public class RequestOptions {
    private final String server;
    private final String user;
    private final String password;
    private final String query;
    private final boolean follow;
    private final int timeout;
    private final int lines;

    public RequestOptions(String server, String user, String password, String query, Integer timeout, Integer lines, boolean follow) {
        this.server = server;
        this.user = user;
        this.password = password;
        this.query = query;
        this.follow = follow;
        this.lines = lines != null ? lines : 20;
        this.timeout = timeout != null ? timeout : 10;
    }

    public String getServer() {
        return server;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getQuery() {
        return query;
    }

    public boolean isFollow() {
        return follow;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getTimeoutInMilliseconds() {
        return getTimeout() * 1000;
    }

    public boolean hasQuery() {
        return !Strings.isNullOrEmpty(query);
    }

    public int getLines() {
        return lines;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RequestOptions)) return false;
        RequestOptions that = (RequestOptions) o;

        return Objects.equal(this.server, that.server) &&
                Objects.equal(this.user, that.user) &&
                Objects.equal(this.password, that.password) &&
                Objects.equal(this.query, that.query) &&
                Objects.equal(this.follow, that.follow) &&
                Objects.equal(this.timeout, that.timeout) &&
                Objects.equal(this.lines, that.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(server, user, password, query, follow, timeout, lines);
    }
}
