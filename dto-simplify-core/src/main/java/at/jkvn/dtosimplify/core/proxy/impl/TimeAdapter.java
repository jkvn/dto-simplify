package at.jkvn.dtosimplify.core.proxy.impl;

import at.jkvn.dtosimplify.core.proxy.TypeAdapter;
import io.swagger.v3.oas.models.media.Schema;

public class TimeAdapter implements TypeAdapter {
    @Override
    public boolean support(Class<?> type) {
        return type == java.time.LocalTime.class || type == java.time.OffsetTime.class;
    }

    @Override
    public Schema<?> toOpenApiSchema(Class<?> type) {
        if (type == java.time.LocalTime.class) {
            return new Schema<>().type("string").format("time");
        } else if (type == java.time.OffsetTime.class) {
            return new Schema<>().type("string").format("time-offset");
        }
        return null;
    }

    @Override
    public Object toJsonValue(Object value, String profile) {
        if (value instanceof java.time.LocalTime) {
            return ((java.time.LocalTime) value).toString();
        } else if (value instanceof java.time.OffsetTime) {
            return ((java.time.OffsetTime) value).toString();
        }
        return value;
    } 
}
