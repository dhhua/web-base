package com.web.base.boot.auth;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Uid {

    // 为false时注入0
    boolean require() default true;

}
