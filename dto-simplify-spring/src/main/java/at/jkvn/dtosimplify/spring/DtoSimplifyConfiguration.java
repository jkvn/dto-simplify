package at.jkvn.dtosimplify.spring;

import at.jkvn.dtosimplify.core.annotation.schema.SchemaResponse;
import at.jkvn.dtosimplify.openapi.generation.DtoSchemaRegistry;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.boot.SpringApplication")
public class DtoSimplifyConfiguration {

    @Bean
    public OpenApiCustomizer dtoSchemaCustomizer(RequestMappingHandlerMapping handlerMapping) {
        return openApi -> {
            Map<String, Schema> schemas = DtoSchemaRegistry.loadSchemasFromMetaFile();
            if (openApi.getComponents() != null) {
                schemas.forEach((name, schema) -> openApi.getComponents().addSchemas(name, schema));
            }

            handlerMapping.getHandlerMethods().forEach((mappingInfo, handlerMethod) -> {
                Method method = handlerMethod.getMethod();
                SchemaResponse annotation = method.getAnnotation(SchemaResponse.class);

                if (annotation != null) {
                    String path = mappingInfo.getPathPatternsCondition()
                            .getPatterns()
                            .stream()
                            .findFirst()
                            .map(Object::toString)
                            .orElse(null);

                    if (path == null) return;

                    openApi.getPaths().get(path).readOperations().forEach(operation -> {
                        ApiResponses responses = operation.getResponses();
                        ApiResponse apiResponse = new ApiResponse();
                        apiResponse.setDescription("Variants: " + Arrays.stream(annotation.variants())
                                .map(v -> v.dto().getSimpleName() + "_" + v.profile())
                                .collect(Collectors.joining(", ")));

                        Schema<Object> variantSchema = new Schema<>();
                        variantSchema.setOneOf(Arrays.stream(annotation.variants())
                                .map(v -> new Schema<>().$ref("#/components/schemas/" + v.dto().getSimpleName() + "_" + v.profile()))
                                .collect(Collectors.toList()));

                        MediaType mediaType = new MediaType().schema(variantSchema);
                        apiResponse.setContent(new Content().addMediaType("application/json", mediaType));

                        responses.addApiResponse("200", apiResponse);
                    });
                }
            });
        };
    }
}
