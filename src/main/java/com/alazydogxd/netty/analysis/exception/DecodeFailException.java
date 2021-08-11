package com.alazydogxd.netty.analysis.exception;

/**
 * @author Mr_W
 * @date 2021/8/12 0:18
 * @description 解析失败
 */
public class DecodeFailException extends Exception {
    public DecodeFailException(String message) {
        super(message);
    }

    public DecodeFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
