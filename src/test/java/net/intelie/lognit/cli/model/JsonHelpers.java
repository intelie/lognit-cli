package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class JsonHelpers {
    public static JsonElement jsonExpected(String input) {
        return new Gson().fromJson(input, JsonElement.class);
    }

    public static <T> JsonElement jsonPrepare(String input, Class<T> type) {
        return jsonElement(new Gson().fromJson(input, type));
    }

    public static JsonElement jsonElement(Object obj) {
        return new Gson().toJsonTree(obj);
    }
}