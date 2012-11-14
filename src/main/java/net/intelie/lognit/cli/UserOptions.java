package net.intelie.lognit.cli;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

public class UserOptions {
    private final String server;
    private final String user;
    private final String password;

    private final String query;

    private final String format;
    private final boolean follow;
    private final boolean info;
    private final boolean help;
    private final boolean purge;
    private final boolean unpurge;
    private final boolean cancelPurges;
    private final boolean all;
    private final boolean pause;
    private final boolean resume;
    private final int timeout;
    private final int lines;
    private final boolean complete;
    private final boolean download;
    private final boolean verbose;
    private final boolean stats;
    private final boolean forceLogin;
    private final boolean metadata;

    public UserOptions(String... args) {
        ArgsParser parser = new ArgsParser(args);
        help = parser.flag("-?", "-h", "--help");
        server = parser.option(String.class, "-s", "--server");
        user = parser.option(String.class, "-u", "--user");
        password = parser.option(String.class, "-p", "--pass", "--password");
        timeout = def(parser.option(Integer.class, "-t", "--timeout"), 30);
        all = parser.flag("--all");
        purge = parser.flag("--purge");
        unpurge = parser.flag("--unpurge");
        cancelPurges = parser.flag("--cancel-purges");
        format = def(parser.option(String.class, "-o", "--output"), "colored");
        follow = parser.flag("-f", "--follow");
        info = parser.flag("-i", "--info");
        complete = parser.flag("-c", "--complete");
        download = parser.flag("-d", "--download");
        lines = def(parser.option(Integer.class, "-n", "--lines"), defaultLine(purge, download));
        verbose = parser.flag("-v", "--verbose");
        stats = parser.flag("-b", "--stats", "--bars");
        pause = parser.flag("--pause");
        resume = parser.flag("--resume");
        forceLogin = parser.flag("--force-login");
        metadata = parser.flag("--meta", "--metadata");
        query = parser.text();
    }

    private int defaultLine(boolean purge, boolean download) {
        return purge ? 20000000 : download ? 100 : 20;
    }

    private <T> T def(T value, T def) {
        return value != null ? value : def;
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

    public boolean hasServer() {
        return server != null;
    }

    public boolean isInfo() {
        return info;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isForceLogin() {
        return forceLogin;
    }

    public boolean askPassword() {
        return user == null || password == null;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isMetadata() {
        return metadata;
    }

    public boolean isPurge() {
        return purge;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public boolean isStats() {
        return stats;
    }

    public String getFormat() {
        return format;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserOptions)) return false;
        UserOptions that = (UserOptions) o;

        return Objects.equal(this.server, that.server) &&
                Objects.equal(this.user, that.user) &&
                Objects.equal(this.password, that.password) &&
                Objects.equal(this.query, that.query) &&
                Objects.equal(this.follow, that.follow) &&
                Objects.equal(this.all, that.all) &&
                Objects.equal(this.purge, that.purge) &&
                Objects.equal(this.unpurge, that.unpurge) &&
                Objects.equal(this.pause, that.pause) &&
                Objects.equal(this.resume, that.resume) &&
                Objects.equal(this.cancelPurges, that.cancelPurges) &&
                Objects.equal(this.timeout, that.timeout) &&
                Objects.equal(this.lines, that.lines) &&
                Objects.equal(this.info, that.info) &&
                Objects.equal(this.format, that.format) &&
                Objects.equal(this.complete, that.complete) &&
                Objects.equal(this.download, that.download) &&
                Objects.equal(this.verbose, that.verbose) &&
                Objects.equal(this.stats, that.stats) &&
                Objects.equal(this.help, that.help) &&
                Objects.equal(this.forceLogin, that.forceLogin) &&
                Objects.equal(this.metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(server, user, password, query, follow, all, pause, resume, purge, unpurge, cancelPurges,
                timeout, lines, info, format, complete, download, verbose, stats, help, forceLogin, metadata);
    }


    public boolean isUnpurge() {
        return unpurge;
    }

    public boolean isCancelPurges() {
        return cancelPurges;
    }

    public boolean isAll() {
        return all;
    }

    public boolean isResume() {
        return resume;
    }

    public boolean isPause() {
        return pause;
    }

    public boolean isDownload() {
        return download;
    }

    public UserOptions realtimeOnly() {
        return new UserOptions(server, user, password, query, format, follow, info, help, purge, unpurge, cancelPurges, all, pause, resume, timeout, 0, complete, download, verbose, forceLogin, stats, metadata);
    }

    public UserOptions(String server, String user, String password, String query, String format, boolean follow, boolean info, boolean help, boolean purge, boolean unpurge, boolean cancelPurges, boolean all, boolean pause, boolean resume, int timeout, int lines, boolean complete, boolean download, boolean verbose, boolean forceLogin, boolean stats, boolean metadata) {
        this.server = server;
        this.user = user;
        this.password = password;
        this.query = query;
        this.format = format;
        this.follow = follow;
        this.info = info;
        this.help = help;
        this.purge = purge;
        this.unpurge = unpurge;
        this.cancelPurges = cancelPurges;
        this.all = all;
        this.pause = pause;
        this.resume = resume;
        this.timeout = timeout;
        this.lines = lines;
        this.complete = complete;
        this.download = download;
        this.verbose = verbose;
        this.forceLogin = forceLogin;
        this.stats = stats;
        this.metadata = metadata;
    }
}
