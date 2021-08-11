package com.alazydogxd.netty.analysis.enums;

/**
 * @author Mr_W
 * @date 2021/7/27 23:36
 * @description 报文字段类型枚举
 */
public enum BaseMessageType {
    /* 字节型 */
    BYTE(Byte.class),
    /* 无符号字节型 */
    UNSIGNED_BYTE(Short.class),
    /* 短整型 */
    SHORT(Short.class),
    /* 无符号短整型 */
    UNSIGNED_SHORT(Integer.class),
    /* 整型 */
    INTEGER(Integer.class),
    /* 无符号整型 */
    UNSIGNED_INTEGER(Long.class),
    /* 长整型 */
    LONG(Long.class),
    /* 单精度浮点数 */
    FLOAT(Float.class),
    /* 双精度浮点数 */
    DOUBLE(Double.class);

    BaseMessageType(Class<?> type) {
        this.type = type;
    }

    private Class<?> type;

    public Class<?> getType() {
        return type;
    }
}
