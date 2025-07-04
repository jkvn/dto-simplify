package at.jkvn.dtosimplify.core.proxy.impl;

import at.jkvn.dtosimplify.core.proxy.TypeAdapter;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

import java.util.UUID;

public class UuidAdapter implements TypeAdapter {
    @Override
    public boolean support(Class<?> type) {
        return type.equals(UUID.class);
    }

    @Override
    public Schema<?> toOpenApiSchema(Class<?> type, String profile) {
        return new StringSchema().format("uuid");
    }

    @Override
    public Object toJsonValue(Object value, String profile) {
        return value.toString();
    }
}
