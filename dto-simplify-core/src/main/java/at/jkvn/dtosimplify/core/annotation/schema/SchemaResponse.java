package at.jkvn.dtosimplify.core.annotation.schema;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SchemaResponse {
    SchemaVariant[] variants();
}