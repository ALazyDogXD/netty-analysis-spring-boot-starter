//package com.alazydogxd.netty.analysis.config;
//
//import com.alazydogxd.netty.analysis.server.UdpServer;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author Mr_W
// * @date 2021/8/20 0:00
// * @description 终端配置
// */
//@Configuration
//public class EndpointConfiguration {
//
//    @Bean
//    @ConditionalOnProperty(name = "spring.lazy-dog.analysis.server.upd.bootstrap", havingValue = "1")
//    public UdpServer updServer(UdpServer udpServer) {
//        udpServer.bootstrap();
//        return udpServer;
//    }
//
//}
