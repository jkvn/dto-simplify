package at.jkvn.dtosimplify.core.metadata;

import java.util.Map;
import java.util.stream.Collectors;

public record ClassMetadata(Class<?> type, Map<String, FieldMetadata> fields) {

    public Map<String, FieldMetadata> getFieldsToView(String profile) {
        return fields.values().stream()
                .filter(metadata -> metadata.isDto(profile))
                .collect(Collectors.toMap(
                        FieldMetadata::getName,
                        field -> field
                ));
    }
}