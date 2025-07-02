package at.jkvn.dtosimplify.core.metadata;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MetadataBuilder {
    
    static ClassMetadata build(Class<?> clazz) {
        Map<String, FieldMetadata> fields = new LinkedHashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            FieldMetadata meta = new FieldMetadata(field);
            fields.put(field.getName(), meta);
        }

        return new ClassMetadata(clazz, fields);
    }
}

