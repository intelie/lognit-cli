package net.intelie.lognit.cli.json;

import com.google.gson.Gson;

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
        return gson.fromJson(json, type);
    }
}
