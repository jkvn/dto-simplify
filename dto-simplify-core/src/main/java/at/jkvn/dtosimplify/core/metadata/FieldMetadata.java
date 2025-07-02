package at.jkvn.dtosimplify.core.metadata;

import at.jkvn.dtosimplify.core.annotation.Dto;

import java.lang.reflect.Field;
import java.util.Arrays;

public record FieldMetadata(Field field) {

    public Object getFieldValue(Object source) throws IllegalAccessException {
        field().setAccessible(true);
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