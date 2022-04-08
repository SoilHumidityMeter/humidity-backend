package com.soilhumidity.backend.util.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiInformation {

    String tag() default "";

    String description() default "";
}
