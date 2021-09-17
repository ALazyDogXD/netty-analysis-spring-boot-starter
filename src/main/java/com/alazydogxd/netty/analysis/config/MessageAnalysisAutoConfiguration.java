package com.alazydogxd.netty.analysis.config;

import com.alazydogxd.netty.analysis.message.BaseSender;
import com.alazydogxd.netty.analysis.message.DefaultSender;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import com.alazydogxd.netty.analysis.message.MessageField;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

/**
 * @author Mr_W
 * @date 2021/8/20 0:31
 * @description 自动配置类
 */
@ComponentScan("com.alazydogxd.netty.analysis")
public class MessageAnalysisAutoConfiguration {

    @Bean
    public MessageAnalysisConfiguration config() {
        return new MessageAnalysisConfiguration()
                .converterPackage("com.alazydogxd.netty.analysis.message");
    }

    @Bean
    @ConditionalOnMissingBean
    public BaseSender<List<MessageField>> sender() {
        return new DefaultSender();
    }

    @Bean
    @ConditionalOnMissingBean
    public ThreadPoolTaskExecutor pool() {
        return new ThreadPoolTaskExecutor();
    }

}
