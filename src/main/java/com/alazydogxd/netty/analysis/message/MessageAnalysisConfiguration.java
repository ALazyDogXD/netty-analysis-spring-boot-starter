package com.alazydogxd.netty.analysis.message;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alazydogxd.netty.analysis.decode.AbstractDecodePattern;
import com.alazydogxd.netty.analysis.decode.MessageFieldDecoder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr_W
 * @date 2021/7/31 19:05
 * @description 配置类
 */
public class MessageAnalysisConfiguration {

    /**
     * <策略唯一标识, 解码策略>
     */
    private final Map<String, AbstractDecodePattern> decodePatterns = new ConcurrentHashMap<>(16);

    /**
     * <类型, 解码器>
     */
    private final Map<String, MessageFieldDecoder<Object>> messageDecoders = new HashMap<>(16);

    /**
     * <所属类别, 报文>
     */
    private final Repository<String, String, MessageField> messageFields = new Repository<>();

    /**
     * 配置报文包路径
     *
     * @param messagePackage 报文包路径
     * @return 配置
     */
    @SuppressWarnings("unchecked")
    public MessageAnalysisConfiguration enumMessagePackage(String messagePackage) {
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(messagePackage, MessageField.class);
        classes.forEach(message -> {
            if (message.isEnum()) {
                List<MessageField> messageFields = (List<MessageField>) Arrays.asList(message.getEnumConstants());
                if (!messageFields.isEmpty()) {
                    this.messageFields.putAll(messageFields.get(0).getSort(),
                            messageFields.get(0).getUniqueMark(),
                            messageFields);
                }
            }
        });
        return this;
    }

    /**
     * 配置解码器包路径
     *
     * @param decoderPackage 解码器包路径
     * @return 配置
     */
    @SuppressWarnings("unchecked")
    public MessageAnalysisConfiguration decoderPackage(String decoderPackage) {
        final Set<Class<?>> classes = ClassUtil.scanPackageBySuper(decoderPackage, MessageFieldDecoder.class);
        classes.forEach(decoder -> {
            MessageFieldDecoder<Object> messageFieldDecoder = (MessageFieldDecoder<Object>) ReflectUtil.newInstance(decoder);
            messageDecoders.put(messageFieldDecoder.type(), messageFieldDecoder);
        });
        return this;
    }

    /**
     * 配置解码模式包路径
     *
     * @param decodePatternPackage 解码模式包路径
     * @return 配置
     */
    public MessageAnalysisConfiguration decodePattern(String decodePatternPackage) {
        final Set<Class<?>> classes = ClassUtil.scanPackageBySuper(decodePatternPackage, AbstractDecodePattern.class);
        classes.forEach(decodePattern -> {
            AbstractDecodePattern pattern = (AbstractDecodePattern) ReflectUtil.newInstance(decodePattern);
            decodePatterns.put(pattern.getPatternName(), pattern);
        });
        return this;
    }

    public boolean isHaveDecoder(String type) {
        return messageDecoders.containsKey(type);
    }

    public MessageFieldDecoder<Object> getMessageDecoder(String type) {
        return messageDecoders.get(type);
    }

    public List<MessageField> getMessageField(String k1, String k2) {
        return messageFields.get(k1, k2);
    }

    static class Repository<K1, K2, V> {
        Map<K1, Map<K2, List<V>>> obj = new HashMap<>(16);

        public void put(K1 k1, K2 k2, V v) {
            checkKey(k1, k2);
            obj.get(k1).get(k2).add(v);
        }

        public void putAll(K1 k1, K2 k2, List<V> v) {
            checkKey(k1, k2);
            obj.get(k1).get(k2).addAll(v);
        }

        private void checkKey(K1 k1, K2 k2) {
            if (!obj.containsKey(k1)) {
                obj.put(k1, new HashMap<>(16));
            }
            if (!obj.get(k1).containsKey(k2)) {
                obj.get(k1).put(k2, new ArrayList<>(15));
            }
        }

        public List<V> get(K1 k1, K2 k2) {
            if (!obj.containsKey(k1) || !obj.get(k1).containsKey(k2)) {
                return new ArrayList<>();
            }
            return obj.get(k1).get(k2);
        }

    }

}
