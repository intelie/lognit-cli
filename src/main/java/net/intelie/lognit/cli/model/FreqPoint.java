package net.intelie.lognit.cli.model;

import com.google.common.base.Objects;

import java.io.Serializable;

public class FreqPoint<T> implements Serializable {
    private final T key;
    private final long freq;

    public FreqPoint(T key, long freq) {
        this.key = key;
        this.freq = freq;
    }

    public T key() {
        return key;
    }

    public long freq() {
        return freq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FreqPoint)) return false;

        FreqPoint that = (FreqPoint) o;

        return Objects.equal(this.key, that.key) &&
                Objects.equal(this.freq, that.freq);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key, freq);
    }

    @Override
    public String toString() {
        return key + ": " + freq;
    }
}
