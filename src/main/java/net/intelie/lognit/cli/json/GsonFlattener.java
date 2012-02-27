package net.intelie.lognit.cli.json;

import com.google.gson.*;

import java.util.Map;

public class GsonFlattener {
    public JsonObject flatten(JsonElement object) {
        return flattenElement(new JsonObject(), "default", object);
    }

    private JsonObject flattenElement(JsonObject target, String key, JsonElement value) {
        if (value.isJsonArray())
            return flattenArray(target, key, value.getAsJsonArray());
        else if (value.isJsonNull())
            return flattenNull(target, key, value.getAsJsonNull());
        else if (value.isJsonObject())
            return flattenObject(target, key, value.getAsJsonObject());
        else if (value.isJsonPrimitive())
            return flattenPrimitive(target, key, value.getAsJsonPrimitive());

        throw new IllegalArgumentException("Invalid json tree '" + value + "'");
    }


    private JsonObject flattenPrimitive(JsonObject target, String key, JsonPrimitive value) {
        return put(target, key, value);
    }

    private JsonObject flattenObject(JsonObject target, String key, JsonObject value) {
        for (Map.Entry<String, JsonElement> entry : value.entrySet())
            target = flattenElement(target, entry.getKey(), entry.getValue());
        return target;
    }

    private JsonObject flattenNull(JsonObject target, String key, JsonNull value) {
        return put(target, key, value);
    }

    private JsonObject flattenArray(JsonObject target, String key, JsonArray value) {
        for (JsonElement element : value)
            target = put(target, key, element);
        return target;
    }

    private JsonObject put(JsonObject target, String key, JsonElement value) {
        if (!target.has(key))
            target.add(key, value);
        return target;
    }
}
