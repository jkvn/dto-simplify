package at.jkvn.dtosimplify.core.annotation.request;

import at.jkvn.dtosimplify.core.annotation.response.DtoView;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DtoRequest {
    DtoField[] value();
}