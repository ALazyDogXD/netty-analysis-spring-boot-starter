package com.alazydogxd.netty.analysis.decode;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author Mr_W
 * @date 2021/8/30 1:22
 * @description 追加报文字段
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface AddMessageField {

    @AliasFor(annotation = Component.class)
    String value() default "";

    String endpointName();

}
