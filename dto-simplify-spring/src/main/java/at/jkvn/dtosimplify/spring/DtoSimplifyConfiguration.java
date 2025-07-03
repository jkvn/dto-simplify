package at.jkvn.dtosimplify.spring;

import at.jkvn.dtosimplify.openapi.generation.DtoSchemaRegistry;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.boot.SpringApplication")
public class DtoSimplifyConfiguration {

    @Bean
    public OpenApiCustomizer dtoSchemaCustomizer() {
        return openApi -> {
            Map<String, Schema> schemas = DtoSchemaRegistry.loadSchemasFromMetaFile();

            if (openApi.getComponents() != null) {
                schemas.forEach((name, schema) -> openApi.getComponents().addSchemas(name, schema));
            } else {
                System.err.println("OpenAPI components not initialized");
            }
        };
    }
}