package com.alazydogxd.netty.analysis.message;

/**
 * @author Mr_W
 * @date 2021/7/27 23:34
 * @description 报文字段信息提供
 */
public interface MessageField {

    /**
     * 获取字段顺序
     *
     * @return 字段顺序
     */
    int getOrder();

    /**
     * 获取字段字节数（-1 不定长度）
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
     * 获取字段默认值或编码值
     *
     * @return 字段默认值或编码值
     */
    Object getValue();

    /**
     * 获取字段类型
     *
     * @return 字段类型
     */
    String getType();

    /**
     * 获取报文所属分类
     *
     * @return 报文所属分类
     */
    String getSort();

    /**
     * 获取报文唯一标识
     *
     * @return 报文唯一标识
     */
    String getUniqueMark();

}
