package net.intelie.lognit.cli.model;

public class SearchChannel {
    private final String channel;
    private final QueryInfo info;

    public SearchChannel(String channel) {
        this(channel, new QueryInfo(true, ""));
    }

    public SearchChannel(String channel, QueryInfo info) {
        this.channel = channel;
        this.info = info;
    }

    public String getChannel() {
        return channel;
    }

    public QueryInfo getInfo() {
        return info;
    }
}
