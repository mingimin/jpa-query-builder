package persistence.sql;

import java.util.HashMap;
import java.util.Map;

public abstract class Dialect {

    private final Map<Integer, String> defaults = new HashMap<>();

    protected void registerColumnType(int code, String name) {
        defaults.put(code, name);
    }

    public String get(final int typeCode) {
        final Integer integer = typeCode;
        final String result = defaults.get(integer);
        if (result == null) {
            throw new IllegalArgumentException("No Dialect mapping for type: " + typeCode);
        }
        return result;
    }
}
