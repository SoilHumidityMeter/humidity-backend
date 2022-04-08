package com.soilhumidity.backend.specs.annotations;

import com.soilhumidity.backend.enums.ERoleType;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiImplicitParams({
        @ApiImplicitParam(name = "search", dataTypeClass = String.class, paramType = "query"),
        @ApiImplicitParam(name = "user", dataTypeClass = Long.class, paramType = "query"),
        @ApiImplicitParam(name = "role", dataTypeClass = ERoleType.class, paramType = "query")
})
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserFilterParams {
}
