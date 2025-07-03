package at.jkvn.dtosimplify.core.proxy;

import io.swagger.v3.oas.models.media.Schema;

public interface TypeAdapter {
    
    boolean support(Class<?> type);
    
    Schema<?> toOpenApiSchema(Class<?> type);
    
    default Object toJsonValue(Object value, String profile) {
        return value;
    }
}
