package at.jkvn.dtosimplify.core.mapping;

import java.util.HashMap;
import java.util.Map;

public class DtoMappingContext {
    
    private final String view;
    private final Map<String, Object> fields = new HashMap<>();

    public DtoMappingContext(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public void set(String key, Object value) {
        fields.put(key, value);
    }

    public Object get(String key) {
        return fields.get(key);
    }

    public Map<String, Object> getFields() {
        return fields;
    }
}
