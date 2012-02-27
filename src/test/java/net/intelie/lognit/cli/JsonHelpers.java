package net.intelie.lognit.cli;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class JsonHelpers {
    public static JsonElement jsonParse(String input) {
        return new Gson().fromJson(input, JsonElement.class);
    }

    public static <T> JsonElement jsonParse(String input, Class<T> type) {
        return jsonElement(new Gson().fromJson(input, type));
    }

    public static JsonElement jsonElement(Object obj) {
        return new Gson().toJsonTree(obj);
    }
}