package net.intelie.lognit.cli.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.MiniGson;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.intelie.lognit.cli.model.Aggregated;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

public class Jsonizer {
    private final Gson gson = new Gson();
    private final GsonFlattener flattener = new GsonFlattener();

    public String to(Object object) {
        return gson.toJson(object);
    }

    public String toFlat(Object object) {
        return flattener.flatten(gson.toJsonTree(object)).toString();
    }

    public <T> T from(String json, Class<T> type) {
        T value = gson.fromJson(json, type);
        return value;
    }

    public <T> Iterator<T> from(final InputStream stream, final Class<T> type) {
        final PushbackInputStream pushStream = new PushbackInputStream(stream, 1);
        final InputStreamReader reader = new InputStreamReader(pushStream);
        final JsonReader jsonReader = new JsonReader(reader);
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                try {
                    int next = pushStream.read();
                    pushStream.unread(next);
                    return next != -1;
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            public T next() {
                return gson.fromJson(jsonReader, type);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
