package at.jkvn.dtosimplify.core.annotation.response;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DtoViews {
    DtoView[] value();
}