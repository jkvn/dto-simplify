package at.jkvn.dtosimplify.openapi.generation;

import at.jkvn.dtosimplify.core.annotation.Dto;
import at.jkvn.dtosimplify.core.metadata.ClassMetadata;
import at.jkvn.dtosimplify.core.metadata.DtoMetadataRegistry;
import io.swagger.v3.oas.models.media.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class SchemaBuilder {
    
    private static final Set<Class<?>> PRIMITIVE_TYPES = Set.of(
        String.class,
        Integer.class, int.class,
        Long.class, long.class,
        Double.class, double.class,
        Float.class, float.class,
        Boolean.class, boolean.class,
        Byte.class, byte.class,
        Short.class, short.class,
        Character.class , char.class
    );

    public static Schema<?> buildSchema(Class<?> dtoClass, String profile) {
        Schema<Object> schema = new ObjectSchema();
        schema.setType("object");

        ClassMetadata metadata = DtoMetadataRegistry.getMetadata(dtoClass);
        metadata.getFieldsToView(profile).forEach((name, field) -> {
            Schema<?> propertySchema = resolveFieldSchema(field.field().getGenericType(), profile);
            schema.addProperties(name, propertySchema);
        });
        
        return schema;
    }

    private static Schema<?> resolveFieldSchema(Type type, String view) {
        if (type instanceof ParameterizedType paramType) {
            return handleParameterizedType(paramType, view);
        } else if (type instanceof Class<?> clazz) {
            return handleClassType(clazz, view);
        } else {
            return new ObjectSchema();
        }
    }

    private static Schema<?> handleParameterizedType(ParameterizedType type, String view) {
        Type rawType = type.getRawType();
        if (!(rawType instanceof Class<?> containerClass)) {
            return new ObjectSchema();
        }

        if (Collection.class.isAssignableFrom(containerClass)) {
            Type itemType = type.getActualTypeArguments()[0];
            Schema<?> itemsSchema = resolveFieldSchema(itemType, view);
            return new ArraySchema().items(itemsSchema);
        }
        
        if (containerClass.equals(Optional.class)) {
            return resolveFieldSchema(type.getActualTypeArguments()[0], view);
        }

        return new ObjectSchema();
    }

    private static Schema<?> handleClassType(Class<?> clazz, String view) {
        if (PRIMITIVE_TYPES.contains(clazz)) {
            return primitiveSchema(clazz);
        }

        if (clazz.isEnum()) {
            return enumSchema(clazz);
        }

        if (clazz.getPackageName().startsWith("java.")) {
            return new StringSchema();
        }
        
        if (clazz.isAnnotationPresent(Dto.class) || clazz.isAnnotationPresent(Dto.Container.class)) {
            return buildSchema(clazz, view);
        }

        return new ObjectSchema();
    }

    private static Schema<?> primitiveSchema(Class<?> clazz) {
        if (clazz.equals(String.class)) return new StringSchema();
        if (clazz.equals(Integer.class) || clazz.equals(int.class)) return new IntegerSchema();
        if (clazz.equals(Long.class) || clazz.equals(long.class)) return new IntegerSchema().format("int64");
        if (clazz.equals(Double.class) || clazz.equals(double.class)) return new NumberSchema().format("double");
        if (clazz.equals(Float.class) || clazz.equals(float.class)) return new NumberSchema().format("float");
        if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) return new BooleanSchema();
        if (clazz.equals(Byte.class) || clazz.equals(byte.class)) return new StringSchema().format("byte");
        if (clazz.equals(Short.class) || clazz.equals(short.class)) return new IntegerSchema();
        return new StringSchema();
    }

    private static Schema<?> enumSchema(Class<?> enumClass) {
        Schema<String> schema = new StringSchema();
        Object[] constants = enumClass.getEnumConstants();
        if (constants != null) {
            List<String> values = new ArrayList<>();
            for (Object constant : constants) {
                values.add(constant.toString());
            }
            schema.setEnum(values);
        }
        return schema;
    }
}