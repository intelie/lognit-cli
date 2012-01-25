package net.intelie.lognit.cli.http;

import com.google.gson.Gson;

public class Jsonizer {
    private final Gson gson;

    public Jsonizer(Gson gson) {
        this.gson = gson;
    }

    public String to(Object object) {
        return gson.toJson(object);
    }

    public <T> T from(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }
}
