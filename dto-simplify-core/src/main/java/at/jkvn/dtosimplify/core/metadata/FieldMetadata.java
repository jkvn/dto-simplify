package at.jkvn.dtosimplify.core.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldMetadata {
    private final Field field;

    public FieldMetadata(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }
    
    public Annotation[] getAnnotations() {
        return field.getAnnotations();
    }
}