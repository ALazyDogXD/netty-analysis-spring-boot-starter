package com.alazydogxd.netty.analysis.config;

import com.alazydogxd.netty.analysis.message.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Mr_W
 * @date 2021/8/20 0:31
 * @description 自动配置类
 */
@EnableAutoConfiguration
@ComponentScan("com.alazydogxd.netty.analysis")
public class MessageAnalysisAutoConfiguration {

    @Bean
    public void config() {
        Configuration config = new Configuration();
    }

}
