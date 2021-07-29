package com.alazydogxd.netty.analysis.annotation;

import java.lang.annotation.*;

/**
 * @author Mr_W
 * @date 2021/7/29 17:06
 * @description 报文解析处理器
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageAnalysisHandler {
}
