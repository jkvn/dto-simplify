package at.jkvn.dtosimplify.core.annotation.response;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(Dto.Container.class)
public @interface Dto {
    
    String value() default "DEFAULT";

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Container {
        Dto[] value();
    }
}