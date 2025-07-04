package at.jkvn.dtosimplify.core.proxy;

import at.jkvn.dtosimplify.core.proxy.impl.TimeAdapter;
import at.jkvn.dtosimplify.core.proxy.impl.UuidAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeAdapterRegistry {
    private static final List<TypeAdapter> typeAdapters = new ArrayList<>();
    
    static {
        TypeAdapterRegistry.registerTypeAdapter(new TimeAdapter());
        TypeAdapterRegistry.registerTypeAdapter(new UuidAdapter());
    }
    
    public static void registerTypeAdapter(TypeAdapter typeAdapter) {
        if (typeAdapter == null) {
            throw new IllegalArgumentException("TypeAdapter cannot be null");
        }
        typeAdapters.add(typeAdapter);
    }
    
    public static void unregisterTypeAdapter(TypeAdapter typeAdapter) {
        if (typeAdapter == null) {
            throw new IllegalArgumentException("TypeAdapter cannot be null");
        }
        typeAdapters.remove(typeAdapter);
    }
    
    public static Optional<TypeAdapter> findTypeAdapter(Class<?> clazz) {
        return typeAdapters.stream()
                .filter(typeAdapter -> typeAdapter.support(clazz))
                .findFirst();
    }
    
    public static List<TypeAdapter> getTypeAdapters() {
        return new ArrayList<>(typeAdapters);
    }
    
    public static void clear() {
        typeAdapters.clear();
    }
}
