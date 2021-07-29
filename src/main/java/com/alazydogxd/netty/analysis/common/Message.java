package com.alazydogxd.netty.analysis.common;

/**
 * @author Mr_W
 * @date 2021/7/27 23:34
 * @description 报文信息提供
 */
public interface Message {

    /**
     * 获取字段顺序
     *
     * @return 字段顺序
     */
    int getOrder();

    /**
     * 获取字段字节数
     *
     * @return 字段字节数
     */
    int getLen();

    /**
     * 获取字段名
     *
     * @return 字段名
     */
    String getFieldName();

    /**
     * 获取字段默认值
     *
     * @return 字段默认值
     */
    Object getDefaultValue();

    /**
     * 获取字段类型
     *
     * @return 字段类型
     */
    String getType();

}
