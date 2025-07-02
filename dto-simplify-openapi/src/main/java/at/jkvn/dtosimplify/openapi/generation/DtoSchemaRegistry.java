package at.jkvn.dtosimplify.openapi.generation;

import at.jkvn.dtosimplify.core.annotation.DtoSchema;
import io.swagger.v3.oas.models.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DtoSchemaRegistry {
    
    private static final Map<String, Schema<?>> SCHEMA_REGISTRY = new ConcurrentHashMap<>();
    
    public static void registerSchema(Class<?> dtoClass) {
        List<DtoSchema> schemaAnnotations = collectSchemaAnnotations(dtoClass);
        if (schemaAnnotations.isEmpty()) return;
        
        schemaAnnotations.forEach(schemaAnnotation -> {
            String view = schemaAnnotation.value();
            String name = schemaAnnotation.name().isBlank() ? schemaAnnotation.name() : dtoClass.getSimpleName() + "_" + view;
            
            Schema<?> schema = SchemaBuilder.buildSchema(dtoClass, view);
            if(!schemaAnnotation.description().isBlank()) {
                schema.setDescription(schemaAnnotation.description());
            }
            
            SCHEMA_REGISTRY.put(name, schema);
        });
    }
    
    public static Schema<?> getSchema(String name) {
        return SCHEMA_REGISTRY.get(name);
    }
    
    public static Map<String, Schema<?>> getAllSchemas() {
        return Map.copyOf(SCHEMA_REGISTRY);
    }
    
    public static void clear() {
        SCHEMA_REGISTRY.clear();
    }
    
    private static List<DtoSchema> collectSchemaAnnotations(Class<?> dtoClass) {
        List<DtoSchema> result = new ArrayList<>();
        if (dtoClass.isAnnotationPresent(DtoSchema.class)) {
            result.add(dtoClass.getAnnotation(DtoSchema.class));
        }
        if (dtoClass.isAnnotationPresent(DtoSchema.Container.class)) {
            result.addAll(List.of(dtoClass.getAnnotation(DtoSchema.Container.class).value()));
        }
        return result;
    }
}