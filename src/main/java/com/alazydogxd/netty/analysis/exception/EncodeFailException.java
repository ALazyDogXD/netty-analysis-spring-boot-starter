package com.alazydogxd.netty.analysis.exception;

/**
 * @author Mr_W
 * @date 2021/9/17 21:18
 * @description 编码失败
 */
public class EncodeFailException extends Exception {
    public EncodeFailException(String message) {
        super(message);
    }

    public EncodeFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
