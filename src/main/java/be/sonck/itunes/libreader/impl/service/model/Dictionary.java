package be.sonck.itunes.libreader.impl.service.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by johansonck on 08/02/16.
 */
public class Dictionary {

    private final String key;
    private final Dictionary parent;
    private final Map<String, Object> values = new HashMap<>();


    public Dictionary(String key, Dictionary parent) {
        this.key = key;
        this.parent = parent;
    }

    public String getKey() {
        return key;
    }

    public Dictionary getParent() {
        return parent;
    }

    public Object getValue(String key) {
        return values.get(key);
    }

    public void put(String key, Object value) {
        values.put(key, value);
    }

    public String getString(String key) {
        return (String) getValue(key);
    }

    public Integer getInteger(String key) {
        Long longValue = (Long) getValue(key);
        return longValue == null ? null : longValue.intValue();
    }

    public Boolean getBoolean(String key) {
        return (Boolean) getValue(key);
    }

    public File getFile(String key) {
        return new File(getString(key));
    }
}
