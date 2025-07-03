package at.jkvn.dtosimplify.core.mapping;

import at.jkvn.dtosimplify.core.metadata.ClassMetadata;
import at.jkvn.dtosimplify.core.metadata.DtoMetadataRegistry;

public class DtoMapper {

    public static DtoMappingContext map(Object source, String profile) {
        if (source == null) return null;

        DtoMappingContext context = new DtoMappingContext(profile);
        ClassMetadata classMetadata = DtoMetadataRegistry.getMetadata(source.getClass());

        classMetadata.getFieldsToView(profile).forEach((key, fieldMetadata) -> {
            try {
                Object fieldValue = fieldMetadata.getFieldValue(source, profile);

                if (fieldValue == null) {
                    context.set(key, null);
                    return;
                }
                
                ClassMetadata fieldClassMetadata = DtoMetadataRegistry.getMetadata(fieldValue.getClass());

                if (!fieldClassMetadata.getFieldsToView(profile).isEmpty()) {
                    DtoMappingContext nestedContext = map(fieldValue, profile);
                    context.set(key, nestedContext.getFields()); 
                } else {
                    context.set(key, fieldValue);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return context;
    }
}