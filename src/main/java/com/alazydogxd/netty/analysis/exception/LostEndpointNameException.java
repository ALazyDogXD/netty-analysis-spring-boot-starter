package com.alazydogxd.netty.analysis.exception;

/**
 * @author Mr_W
 * @date 2021/8/30 1:34
 * @description 丢失终端名称异常
 */
public class LostEndpointNameException extends RuntimeException {

    public LostEndpointNameException(String message) {
        super(message);
    }
}
