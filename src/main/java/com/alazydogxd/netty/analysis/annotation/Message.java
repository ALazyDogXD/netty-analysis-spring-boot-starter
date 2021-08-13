package com.alazydogxd.netty.analysis.annotation;

import java.lang.annotation.*;

/**
 * @author Mr_W
 * @date 2021/7/31 19:15
 * @description 报文
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Message {

    /**
     * netty 服务名称
     */
    String serverName();

    /**
     * 报文唯一标识
     */
    String value();

}
