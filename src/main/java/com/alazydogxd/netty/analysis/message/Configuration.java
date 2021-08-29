package com.alazydogxd.netty.analysis.message;

import cn.hutool.core.util.ClassUtil;
import com.alazydogxd.netty.analysis.decode.AbstractDecodePattern;
import com.alazydogxd.netty.analysis.decode.MessageFieldDecoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr_W
 * @date 2021/7/31 19:05
 * @description 配置类
 */
public class Configuration {

    /** <服务名, 解码策略> */
    private final Map<String, AbstractDecodePattern> messageDepot = new ConcurrentHashMap<>(16);

    /** <类型, 解码器> */
    private final Map<String, MessageFieldDecoder<Object>> msgDecoders = new HashMap<>(16);

    /**
     * 配置报文包路径
     *
     * @param messagePackage 报文包路径
     * @return 配置
     */
    public Configuration msgPackage(String messagePackage) {
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(messagePackage, MessageField.class);
        classes.forEach(msg -> {

        });
        return this;
    }

    public Map<String, MessageFieldDecoder<Object>> getMsgDecoders() {
        return msgDecoders;
    }

}
