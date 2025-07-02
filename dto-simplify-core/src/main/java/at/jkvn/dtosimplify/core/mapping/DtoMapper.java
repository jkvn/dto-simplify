package at.jkvn.dtosimplify.core.mapping;

import at.jkvn.dtosimplify.core.metadata.ClassMetadata;
import at.jkvn.dtosimplify.core.metadata.DtoMetadataRegistry;

public class DtoMapper {
    
    public static DtoMappingContext map(Object source, String profile) {
        DtoMappingContext context = new DtoMappingContext(profile);
        ClassMetadata classMetadata = DtoMetadataRegistry.getMetadata(source.getClass());

        classMetadata.getFieldsToView(profile).forEach((key, fieldMetadata) -> {
            try {
                System.out.println(fieldMetadata.getFieldValue(source));
                context.set(key, fieldMetadata.getFieldValue(source));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });


        return context;
    }
}
