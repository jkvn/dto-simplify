package at.jkvn.dtosimplify.openapi.generation;

import at.jkvn.dtosimplify.core.annotation.Dto;
import at.jkvn.dtosimplify.core.annotation.DtoSchema;
import at.jkvn.dtosimplify.core.metadata.ClassMetadata;
import at.jkvn.dtosimplify.core.metadata.DtoMetadataRegistry;
import io.swagger.v3.oas.models.media.*;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DtoSchemaRegistry {

    public static Schema getSchema(String name) {
        return loadSchemasFromMetaFile().get(name);
    }

    public static Map<String, Schema> getAllSchemas() {
        return loadSchemasFromMetaFile();
    }

    public static Map<String, Schema> loadSchemasFromMetaFile() {
        System.out.println("Loading DTO schemas from META-INF/dtosimplify.schemas");
        Map<String, Schema> schemas = new LinkedHashMap<>();

        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("META-INF/dtosimplify.schemas")) {

            if (in == null) {
                return schemas;
            }

            List<String> classNames = new BufferedReader(new InputStreamReader(in))
                    .lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .toList();

            for (String className : classNames) {
                try {
                    Class<?> clazz = Class.forName(className);
                    processDtoSchemaAnnotations(clazz, schemas);
                } catch (ClassNotFoundException e) {
                    System.err.println("DTO class not found: " + className);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error while reading dtosimplify.schemas", e);
        }

        return schemas;
    }

    private static void processDtoSchemaAnnotations(Class<?> clazz, Map<String, Schema> schemas) {
        DtoSchema[] annotations;

        if (clazz.isAnnotationPresent(DtoSchema.Container.class)) {
            annotations = clazz.getAnnotation(DtoSchema.Container.class).value();
        } else if (clazz.isAnnotationPresent(DtoSchema.class)) {
            annotations = new DtoSchema[]{ clazz.getAnnotation(DtoSchema.class) };
        } else {
            return;
        }

        for (DtoSchema annotation : annotations) {
            String profile = annotation.value();
            String schemaName = annotation.name().isEmpty()
                    ? clazz.getSimpleName() + "_" + profile
                    : annotation.name();

            String description = annotation.description().isEmpty()
                    ? "Generated from " + clazz.getName()
                    : annotation.description();

            System.out.println("Generating schema: " + schemaName);

            ObjectSchema schema = (ObjectSchema) new ObjectSchema()
                    .name(schemaName)
                    .description(description);

            for (Field field : clazz.getDeclaredFields()) {
                for (Dto dto : field.getAnnotationsByType(Dto.class)) {
                    if (dto.value().equals(profile)) {
                        Schema<?> fieldSchema = getSchemaFromJavaType(field.getType(), profile);
                        schema.addProperty(field.getName(), fieldSchema);
                        break;
                    }
                }
            }

            schemas.put(schemaName, schema);
        }
    }

    private static Schema<?> getSchemaFromJavaType(Class<?> fieldType, String profile) {
        if (fieldType.equals(String.class)) {
            return new StringSchema();
        } else if (fieldType.isPrimitive()
                || fieldType.equals(Integer.class)
                || fieldType.equals(Long.class)
                || fieldType.equals(Short.class)
                || fieldType.equals(Byte.class)) {
            return new IntegerSchema();
        } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
            return new BooleanSchema();
        } else if (fieldType.equals(Double.class)
                || fieldType.equals(Float.class)
                || fieldType.equals(BigDecimal.class)) {
            return new NumberSchema();
        } else if (fieldType.equals(LocalDate.class)) {
            return new StringSchema().format("date");
        } else if (fieldType.equals(LocalDateTime.class) || fieldType.equals(Date.class)) {
            return new StringSchema().format("date-time");
        } else if (Collection.class.isAssignableFrom(fieldType)) {
            return new ArraySchema().items(new StringSchema());
        } else if (Map.class.isAssignableFrom(fieldType)) {
            return new ObjectSchema().additionalProperties(new StringSchema());
        } else {
            try {
                ClassMetadata nestedMetadata = DtoMetadataRegistry.getMetadata(fieldType);
                if (!nestedMetadata.getFieldsToView(profile).isEmpty()) {
                    return new Schema<>().$ref("#/components/schemas/" + fieldType.getSimpleName() + "_" + profile);
                }
            } catch (Exception ignored) {
            }

            return new ObjectSchema();
        }
    }

    public static void writeMetaInfFile(Collection<String> classNames, Filer filer, Messager messager) {
        try {
            FileObject file = filer.createResource(
                    StandardLocation.CLASS_OUTPUT,
                    "",
                    "META-INF/dtosimplify.schemas"
            );

            try (Writer writer = file.openWriter()) {
                for (String clazz : classNames) {
                    writer.write(clazz + "\n");
                    messager.printMessage(Diagnostic.Kind.NOTE, "Registered DTO schema: " + clazz);
                }
            }

        } catch (IOException e) {
            messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Failed to write META-INF/dtosimplify.schemas: " + e.getMessage()
            );
        }
    }
}