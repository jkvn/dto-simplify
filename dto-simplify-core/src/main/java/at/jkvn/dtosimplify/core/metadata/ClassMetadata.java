package at.jkvn.dtosimplify.core.metadata;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassMetadata {

    private final Class<?> type;
    private final Map<String, FieldMetadata> fields;

    public ClassMetadata(Class<?> type, Map<String, FieldMetadata> fields) {
        this.type = type;
        this.fields = fields;
    }

    public Class<?> getType() {
        return type;
    }

    public Map<String, FieldMetadata> getFields() {
        return fields;
    }

    public Map<String, FieldMetadata> getFieldsToView(String profile) {
        return fields.values().stream()
                .filter(metadata -> metadata.isDto(profile))
                .collect(Collectors.toMap(
                        FieldMetadata::getName,
                        field -> field
                ));
    }
}