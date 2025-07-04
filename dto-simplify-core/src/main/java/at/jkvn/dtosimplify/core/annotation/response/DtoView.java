package at.jkvn.dtosimplify.core.annotation.response;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(DtoViews.class)
public @interface DtoView {
    String value();
    
    String[] include();
}