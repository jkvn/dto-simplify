package at.jkvn.dtosimplify.core.mapping;

import at.jkvn.dtosimplify.core.metadata.ClassMetadata;
import at.jkvn.dtosimplify.core.metadata.DtoMetadataRegistry;

import java.util.*;

public class DtoMapper {

    public static Object map(Object source, String profile) {
        return mapDynamic(source, profile, true);
    }

    private static Object mapFieldValue(Object value, String profile) {
        return mapDynamic(value, profile, false);
    }

    private static Object mapDynamic(Object value, String profile, boolean allowDto) {
        if (value == null) return null;

        if (value.getClass().isArray()) {
            return mapIterable(Arrays.asList((Object[]) value), profile);
        }

        if (value instanceof Collection<?> collection) {
            return mapIterable(collection, profile);
        }

        if (value instanceof Map<?, ?> map) {
            return mapMap(map, profile);
        }

        if (allowDto) {
            return mapDto(value, profile);
        }

        return mapElement(value, profile);
    }

    private static List<Object> mapIterable(Iterable<?> items, String profile) {
        List<Object> result = new ArrayList<>();
        for (Object item : items) {
            result.add(mapElement(item, profile));
        }
        return result;
    }

    private static Map<Object, Object> mapMap(Map<?, ?> map, String profile) {
        Map<Object, Object> result = new LinkedHashMap<>();
        map.forEach((key, value) -> result.put(key, mapElement(value, profile)));
        return result;
    }

    private static Map<String, Object> mapDto(Object dto, String profile) {
        DtoMappingContext context = new DtoMappingContext(profile);
        ClassMetadata metadata = DtoMetadataRegistry.getMetadata(dto.getClass());

        metadata.getFieldsToView(profile).forEach((key, field) -> {
            try {
                Object raw = field.getFieldValue(dto, profile);
                context.set(key, mapFieldValue(raw, profile));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return context.getFields();
    }

    private static Object mapElement(Object value, String profile) {
        if (value == null) return null;

        ClassMetadata metadata = DtoMetadataRegistry.getMetadata(value.getClass());
        return metadata.getFieldsToView(profile).isEmpty()
                ? value
                : mapDto(value, profile);
    }
}