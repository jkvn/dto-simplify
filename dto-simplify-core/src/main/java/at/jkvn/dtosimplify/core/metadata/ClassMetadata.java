package at.jkvn.dtosimplify.core.metadata;

import java.util.List;
import java.util.Map;

public class ClassMetadata {

    private final Class<?> type;
    private final Map<String, FieldMetadata> fields;
    private final Map<String, List<String>> viewFieldMapping;

    public ClassMetadata(Class<?> type, Map<String, FieldMetadata> fields, Map<String, List<String>> viewFieldMapping) {
        this.type = type;
        this.fields = fields;
        this.viewFieldMapping = viewFieldMapping;
    }

    public Class<?> getType() {
        return type;
    }

    public Map<String, FieldMetadata> getFields() {
        return fields;
    }

    public Map<String, List<String>> getViewFieldMapping() {
        return viewFieldMapping;
    }

    public List<String> getFieldsForView(String view) {
        return viewFieldMapping.getOrDefault(view, List.of());
    }

    public boolean isFieldInView(String fieldName, String view) {
        return getFieldsForView(view).contains(fieldName);
    }
}