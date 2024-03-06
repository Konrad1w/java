package uj.wmii.pwj.collections;

import java.util.List;
import java.util.Map;

public interface JsonMapper {

    static JsonMapper defaultInstance() {
        return new JsonMapper() {
            @Override
            public String toJson(Map<String, ?> map) {
                StringBuilder result = new StringBuilder("{\n");
                if (map != null)
                    for (Map.Entry<String, ?> entry : map.entrySet())
                        result.append(writeInJSON(entry.getKey(), entry.getValue()));
                result.append("\n}");
                return result.toString();
            }
        };
    }

    static String writeInJSON(String key, Object o) {
        String result = "\"" + key + "\": " +
                changeObjectToJSON(o);
        return result;
    }

    static StringBuilder changeObjectToJSON(Object o) {
        if (o instanceof String)
            return new StringBuilder().append('"').append(o).append('"');
        else if (o instanceof List<?>)
            return listToJSON((List<?>) o);
        else if (o instanceof Map<?, ?>)
            return mapToJSON((Map<String, ?>) o);
        else
            return new StringBuilder().append(o);
    }

    static StringBuilder listToJSON(List<?> list) {
        StringBuilder result = new StringBuilder().append('[');
        for (Object o : list) {
            result.append(changeObjectToJSON(o));
            result.append(", ");
        }
        if (result.length() >= 2)
            result.deleteCharAt(result.length() - 2).setCharAt(result.length() - 1, ']');
        else
            result.append(']');
        return result;
    }

    static StringBuilder mapToJSON(Map<String, ?> map) {
        StringBuilder result = new StringBuilder().append("{\n");
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            result.append("\"").append(entry.getKey()).append("\": ");
            result.append(changeObjectToJSON(entry.getValue())).append(",\n");
        }
        if (result.length() >= 2)
            result.deleteCharAt(result.length() - 2).append("}\n");
        else result.append('}');
        return result;
    }

    String toJson(Map<String, ?> map);

}
