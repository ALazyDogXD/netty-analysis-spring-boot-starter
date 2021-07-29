package com.alazydogxd.netty.analysis.annotation;

import java.lang.annotation.*;

/**
 * @author Mr_W
 * @date 2021/7/29 22:36
 * @description 字符格式
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CharFormat {

    String value() default "UFT-8";

}
