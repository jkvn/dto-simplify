package at.jkvn.dtosimplify.core.metadata;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MetadataBuilder {
    
    static ClassMetadata build(Class<?> clazz) {
        Map<String, FieldMetadata> fields = new LinkedHashMap<>();
        Map<String, List<String>> viewToFields = new LinkedHashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            //Dto[] annotations = field.getAnnotationsByType(Dto.class);
            //if (annotations.length == 0) continue;

            //Set<String> views = new HashSet<>();
            //for (Dto dto : annotations) {
            //    views.add(dto.value());
            //}

            //boolean nested = field.getType().isAnnotationPresent(Dto.class);
            FieldMetadata meta = new FieldMetadata(field);
            fields.put(field.getName(), meta);

            //for (String view : views) {
            //    viewToFields.computeIfAbsent(view, k -> new ArrayList<>()).add(field.getName());
            //}
        }

        return new ClassMetadata(clazz, fields, viewToFields);
    }
}

