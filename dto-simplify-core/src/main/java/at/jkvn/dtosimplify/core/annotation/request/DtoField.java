package at.jkvn.dtosimplify.core.annotation.request;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(DtoRequest.class)
public @interface DtoField {
    String[] value();
}