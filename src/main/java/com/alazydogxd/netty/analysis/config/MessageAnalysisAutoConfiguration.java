package com.alazydogxd.netty.analysis.config;

import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Mr_W
 * @date 2021/8/20 0:31
 * @description 自动配置类
 */
@ComponentScan("com.alazydogxd.netty.analysis")
public class MessageAnalysisAutoConfiguration {

    @Bean
    public MessageAnalysisConfiguration config() {
        return new MessageAnalysisConfiguration();
    }

}
