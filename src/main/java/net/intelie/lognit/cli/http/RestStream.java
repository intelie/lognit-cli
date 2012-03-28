package net.intelie.lognit.cli.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class RestStream<T> implements Iterator<T> {
    private final Iterator<T> iterator;
    private final InputStream stream;

    public RestStream(Iterator<T> iterator, InputStream stream) {
        this.iterator = iterator;
        this.stream = stream;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    public void close() {
        try {
            if (stream != null)
                stream.close();
        } catch (IOException e) {
        }
    }
}
