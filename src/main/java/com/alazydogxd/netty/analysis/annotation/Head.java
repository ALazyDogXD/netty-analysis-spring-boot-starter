package com.alazydogxd.netty.analysis.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Mr_W
 * @date 2021/8/5 1:01
 * @description 报文头
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Head {

}
