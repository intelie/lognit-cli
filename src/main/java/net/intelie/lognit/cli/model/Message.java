package net.intelie.lognit.cli.model;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Message implements Comparable<Message> {
    private static final Comparator COMPARATOR = Ordering.from(String.CASE_INSENSITIVE_ORDER).reverse().nullsLast();

    private final String id;
    private final String host;
    private final String date;
    private final String time;
    private final String facility;
    private final String severity;
    private final String app;
    private final String message;
    private final Map<String, List<String>> metadata;

    public Message(String id) {
        this(id, null, null, null, null, null, null, null, null);
    }

    public Message(String id, String host, String date, String time, String facility, String severity, String app, String message, Map<String, List<String>> metadata) {
        this.id = id;
        this.host = host;
        this.date = date;
        this.time = time;
        this.facility = facility;
        this.severity = severity;
        this.app = app;
        this.message = message;
        this.metadata = metadata;
    }

    public String formattedDateTime() {
        if (date == null || time == null || date.length() != 8 || time.length() != 6) return date + time;
        return String.format("%s %s %s:%s:%s",
                Months.forNumber(date.substring(4, 6)), date.substring(6, 8),
                time.substring(0, 2), time.substring(2, 4), time.substring(4, 6));
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getFacility() {
        return facility;
    }

    public String getApp() {
        return app;
    }

    @Override
    public int compareTo(Message that) {
        return COMPARATOR.compare(this.id, that.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message that = (Message) o;

        return Objects.equal(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getSeverity() {
        return severity;
    }

    public Map<String, List<String>> getMetadata() {
        return metadata;
    }
}
