package com.alazydogxd.netty.analysis.exception;

/**
 * @author Mr_W
 * @date 2021/7/29 22:23
 * @description 不存在的报文字段类型
 */
public class UnExistMessageTypeException extends RuntimeException {

    public UnExistMessageTypeException(String message) {
        super(message);
    }
}
