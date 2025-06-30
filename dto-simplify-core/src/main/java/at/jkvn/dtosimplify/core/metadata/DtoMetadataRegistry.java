package at.jkvn.dtosimplify.core.metadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DtoMetadataRegistry {

    private static final Map<Class<?>, ClassMetadata> cache = new ConcurrentHashMap<>();

    public static ClassMetadata getMetadata(Class<?> clazz) {
        return cache.computeIfAbsent(clazz, MetadataBuilder::build);
    }
}
