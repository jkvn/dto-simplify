package at.jkvn.dtosimplify.core.annotation.schema;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface SchemaVariant {
    Class<?> dto();
    String profile();
}