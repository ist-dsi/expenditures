package pt.ist.internalBilling.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {

    public static <T extends JsonElement> Collector<T, JsonArray, JsonArray> toJsonArray() {
        return Collector.of(JsonArray::new, (array, element) -> array.add(element), (one, other) -> {
            one.addAll(other);
            return one;
        }, Characteristics.IDENTITY_FINISH);
    }

    public static Map<String, String> toMap(final String json, final String key, final String value) {
        final JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        return toMap(array, key, value);
    }

    public static Map<String, String> toMap(final JsonArray array, final String key, final String value) {
        final Map<String, String> map = new HashMap<String, String>();
        array.forEach(e -> put(map, e, key, value));
        return map;        
    }

    private static void put(final Map<String, String> map, final JsonElement e, final String key, final String value) {
        final JsonObject o = e.getAsJsonObject();
        map.put(o.get(key).getAsString(), o.get(value).getAsString());
    }

}
