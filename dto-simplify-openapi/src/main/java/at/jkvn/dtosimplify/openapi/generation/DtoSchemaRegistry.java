package at.jkvn.dtosimplify.openapi.generation;

import at.jkvn.dtosimplify.core.annotation.Dto;
import at.jkvn.dtosimplify.core.annotation.DtoSchema;
import io.swagger.v3.oas.models.media.Schema;

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

                    if (clazz.isAnnotationPresent(DtoSchema.Container.class)) {
                        DtoSchema.Container container = clazz.getAnnotation(DtoSchema.Container.class);

                        for (DtoSchema annotation : container.value()) {
                            String schemaName = annotation.name().isEmpty()
                                    ? clazz.getSimpleName() + "_" + annotation.value()
                                    : annotation.name();

                            String description = annotation.description().isEmpty()
                                    ? "Generated from " + clazz.getName()
                                    : annotation.description();

                            Schema<?> schema = new Schema<>()
                                    .name(schemaName)
                                    .description(description);

                            for (Field field : clazz.getDeclaredFields()) {
                                for (Dto dto : field.getAnnotationsByType(Dto.class)) {
                                    if (dto.value().equals(annotation.value())) {
                                        Schema<?> fieldSchema = getSchemaFromJavaType(field.getType());
                                        schema.addProperties(field.getName(), fieldSchema);
                                        break;
                                    }
                                }
                            }

                            schemas.put(schemaName, schema);
                        }
                    }

                } catch (ClassNotFoundException e) {
                    System.err.println("DTO class not found: " + className);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error while reading dtosimplify.schemas", e);
        }

        return schemas;
    }

    private static Schema<?> getSchemaFromJavaType(Class<?> fieldType) {
        if (fieldType.equals(String.class)) {
            return new Schema<>().type("string");
        } else if (fieldType.isPrimitive() ||
                fieldType.equals(Integer.class) ||
                fieldType.equals(Long.class) ||
                fieldType.equals(Short.class) ||
                fieldType.equals(Byte.class)) {
            return new Schema<>().type("integer");
        } else if (fieldType.equals(Boolean.class) || fieldType.isAssignableFrom(boolean.class)) {
            return new Schema<>().type("boolean");
        } else if (fieldType.equals(Double.class) ||
                fieldType.equals(Float.class) ||
                fieldType.equals(BigDecimal.class)) {
            return new Schema<>().type("number");
        } else if (fieldType.equals(LocalDate.class)) {
            return new Schema<>().type("string").format("date");
        } else if (fieldType.equals(LocalDateTime.class) || fieldType.equals(Date.class)) {
            return new Schema<>().type("string").format("date-time");
        } else if (Collection.class.isAssignableFrom(fieldType)) {
            Schema<?> itemsSchema = new Schema<>().type("string");
            return new Schema<>().type("array").items(itemsSchema);
        } else if (Map.class.isAssignableFrom(fieldType)) {
            return new Schema<>().type("object").additionalProperties(new Schema<>());
        } else {
            return new Schema<>().$ref("#/components/schemas/" + fieldType.getSimpleName());
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