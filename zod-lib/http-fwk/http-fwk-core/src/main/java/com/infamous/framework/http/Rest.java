package com.infamous.framework.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Rest {

    HttpMethod method() default HttpMethod.GET;

    String url() default "";

    String contentType() default "application/json";
}

