package at.jkvn.dtosimplify.core.metadata;

import at.jkvn.dtosimplify.core.annotation.Dto;
import at.jkvn.dtosimplify.core.proxy.TypeAdapter;
import at.jkvn.dtosimplify.core.proxy.TypeAdapterRegistry;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public record FieldMetadata(Field field) {
    
    public Object getFieldValue(Object source, String profile) throws IllegalAccessException {
        field().setAccessible(true);
        if (source == null) {
            return null;
        }
        
        Optional<TypeAdapter> optionalTypeAdapter = TypeAdapterRegistry.findTypeAdapter(field.getType());
        if (optionalTypeAdapter.isPresent()) {
            TypeAdapter typeAdapter = optionalTypeAdapter.get();
            return typeAdapter.toJsonValue(field.get(source), profile);
        }
        return field.get(source);
    }

    public boolean isDto(String profile) {
        return Arrays.stream(field.getAnnotationsByType(Dto.class))
                .anyMatch(dto -> dto.value().equals(profile));
    }

    public String getName() {
        return field().getName();
    }
}