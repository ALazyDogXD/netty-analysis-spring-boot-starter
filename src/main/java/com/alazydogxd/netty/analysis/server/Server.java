package com.alazydogxd.netty.analysis.server;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AliasFor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author Mr_W
 * @date 2021/8/17 23:36
 * @description 服务端
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Scope
public @interface Server {

    @AliasFor(annotation = Component.class)
    String value();

    @AliasFor(annotation = Scope.class, attribute = "scopeName")
    String scopeName() default ConfigurableBeanFactory.SCOPE_PROTOTYPE;

}
