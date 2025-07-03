package at.jkvn.dtosimplify.core.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(DtoSchema.Container.class)
public @interface DtoSchema {
    String value();
    String name() default "";
    String description() default "";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Container {
        DtoSchema[] value();
    }
}
