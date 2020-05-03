package com.web.base.boot.auth;

/**
 * @author dong
 * @date 2020/5/1
 */
public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super("token was expired");
    }

}
