package at.jkvn.dtosimplify.core.proxy.impl;

import at.jkvn.dtosimplify.core.proxy.TypeAdapter;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.Schema;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

public class TimeAdapter implements TypeAdapter {

    private static final Map<Class<?>, String> formatMap = new HashMap<>();

    static {
        formatMap.put(LocalDate.class, "date");
        formatMap.put(LocalDateTime.class, "date-time-local");
        formatMap.put(LocalTime.class, "time");
        formatMap.put(OffsetTime.class, "time-offset");
        formatMap.put(ZonedDateTime.class, "date-time");
        formatMap.put(Instant.class, "instant");
    }

    @Override
    public boolean support(Class<?> type) {
        return formatMap.containsKey(type);
    }

    @Override
    public Schema<?> toOpenApiSchema(Class<?> type) {
        String format = formatMap.get(type);
        return new StringSchema().format(format);
    }
    
    @Override
    public Object toJsonValue(Object value, String profile) {
        return value.toString();
    }
}