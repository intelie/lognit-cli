package net.intelie.lognit.cli.model;

import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

import java.util.Comparator;

public class Message implements Comparable<Message> {
    private static final Comparator COMPARATOR = new NullComparator(
            new ReverseComparator(String.CASE_INSENSITIVE_ORDER), true);
    private final String id;
    private final String host;
    private final String date;
    private final String time;
    private final String facility;
    private final String severity;
    private final String app;
    private final String message;

    public Message(String id) {
        this(id, null, null, null, null, null, null, null);
    }

    public Message(String id, String host, String date, String time, String facility, String severity, String app, String message) {
        this.id = id;
        this.host = host;
        this.date = date;
        this.time = time;
        this.facility = facility;
        this.severity = severity;
        this.app = app;
        this.message = message;
    }

    public String formattedDateTime() {
        if (date.length() != 8 || time.length() != 6) return date + time;
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

    public String getSeverity() {
        return severity;
    }

}
