package at.jkvn.dtosimplify.core.metadata;

import at.jkvn.dtosimplify.core.annotation.Dto;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

public class FieldMetadata {
    private final Field field;

    public FieldMetadata(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public Object getFieldValue(Object source) throws IllegalAccessException {
        getField().setAccessible(true);
        return field.get(source);
    }
    
    public Annotation[] getAnnotations() {
        return field.getAnnotations();
    }

    public boolean isDto(String profile) {
        return Arrays.stream(getAnnotations())
                .filter(annotation -> annotation.annotationType() == Dto.class)
                .map(annotation -> (Dto) annotation)
                .peek(test -> System.out.println(test))
                .anyMatch(dto -> dto.value().equals(profile));
    }

    public String getName() {
        return getField().getName();
    }
}