package com.web.base.common.base;

/**
 * @author dong
 * @date 2020/5/10
 */
public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
