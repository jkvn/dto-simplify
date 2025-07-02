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
                                        schema.addProperties(field.getName(), new Schema<>());
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