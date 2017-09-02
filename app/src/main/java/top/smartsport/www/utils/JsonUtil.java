package top.smartsport.www.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class JsonUtil {
    private static Gson gson = new Gson();

    public static <T> T jsonToEntity(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> List<T> jsonToEntityList(String json, Class<T> classOfT) {
        JsonParser parser = new JsonParser();
        try {
            JsonElement ele = parser.parse(json);
            return jsonToEntityList(ele.getAsJsonArray(), classOfT);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> jsonToEntityList(JsonArray arr, Class<T> classOfT) {
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(jsonToEntity(arr.get(i).toString(), classOfT));
        }
        return list;

    }

    public static String entityToJson(Object src) {
        return gson.toJson(src);
    }

    public static String sortJson(String json) {
        JsonElement ele = new JsonParser().parse(json);
        if (ele.isJsonObject()) {
            return sortJsonObj(ele.getAsJsonObject()).toString();
        }
        return null;
    }

    public static JsonObject sortJsonObj(JsonObject obj) {
        List<Map.Entry<String, JsonElement>> list = new ArrayList<Map.Entry<String, JsonElement>>();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            list.add(entry);
        }
        Collections.sort(list, comp);
        obj = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : list) {
            JsonElement value = entry.getValue();
            if (value.isJsonArray()) {
                value = sortJsonArray(value.getAsJsonArray());
            } else if (value.isJsonObject()) {
                value = sortJsonObj(value.getAsJsonObject());
            }
            obj.add(entry.getKey(), value);
        }
        return obj;
    }

    public static JsonArray sortJsonArray(JsonArray arr) {
        if (arr.size() <= 0 || !arr.get(0).isJsonObject()) {
            return arr;
        }
        JsonArray newArr = new JsonArray();
        for (int i = 0; i < arr.size(); i++) {
            JsonObject obj = arr.get(i).getAsJsonObject();
            newArr.add(sortJsonObj(obj));
        }
        return newArr;
    }

    private static Comparator<Map.Entry<String, JsonElement>> comp = new Comparator<Map.Entry<String, JsonElement>>() {
        @Override
        public int compare(Map.Entry<String, JsonElement> lhs, Map.Entry<String, JsonElement> rhs) {
            int[] lASC = getKeyASCII(lhs.getKey());
            int[] rASC = getKeyASCII(rhs.getKey());
            int length = lASC.length < rASC.length ? lASC.length : rASC.length;
            for (int i = 0; i < length; i++) {
                if (lASC[i] != rASC[i]) {
                    return lASC[i] < rASC[i] ? -1 : 1;
                }
            }
            return lASC.length < rASC.length ? -1 : 1;
        }
    };

    private static int[] getKeyASCII(String key) {
        char[] chars = key.toCharArray();
        int[] ascii = new int[chars.length];
        for (int i = 0; i < ascii.length; i++) {
            ascii[i] = (int) chars[i];
        }
        return ascii;
    }
}

