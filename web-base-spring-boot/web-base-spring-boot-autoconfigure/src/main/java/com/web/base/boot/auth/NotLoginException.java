package com.web.base.boot.auth;

/**
 * @author dong
 * @date 2020/5/1
 */
public class NotLoginException extends RuntimeException {

    public NotLoginException() {
        super("should be login first");
    }
}
