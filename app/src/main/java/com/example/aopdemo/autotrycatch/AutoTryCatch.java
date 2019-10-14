package com.example.aopdemo.autotrycatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author:  ycl
 * date:  2019/10/14 17:40
 * desc:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoTryCatch {
    Class[] value() default Exception.class;
}
