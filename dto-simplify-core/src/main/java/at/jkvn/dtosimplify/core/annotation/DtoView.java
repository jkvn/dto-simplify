package at.jkvn.dtosimplify.core.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DtoView {
    String value() default "";
    
}