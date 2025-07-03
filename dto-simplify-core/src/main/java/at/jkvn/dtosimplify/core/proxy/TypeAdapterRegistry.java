package at.jkvn.dtosimplify.core.proxy;

import at.jkvn.dtosimplify.core.proxy.impl.UuidAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeAdapterRegistry {
    static {
        TypeAdapterRegistry.registerTypeAdapter(new UuidAdapter());
    }
    
    private static List<TypeAdapter> typeAdapters = new ArrayList<>();
    
    public static void registerTypeAdapter(TypeAdapter typeAdapter) {
        if (typeAdapter == null) {
            throw new IllegalArgumentException("TypeAdapter cannot be null");
        }
        typeAdapters.add(typeAdapter);
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
