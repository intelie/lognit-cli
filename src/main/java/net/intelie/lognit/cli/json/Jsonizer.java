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

import java.io.*;
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
        final JsonReader jsonReader = new JsonReader(new InputStreamReader(stream));
        jsonReader.setLenient(true);

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                try {
                    return jsonReader.peek() != JsonToken.END_DOCUMENT;
                } catch (EOFException e) {
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
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
