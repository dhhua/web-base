package com.web.base.boot.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dong
 * @date 2020/5/1
 */
@Data
@ConfigurationProperties(prefix = "boot.auth")
public class TokenProperties {

    private Long tokenExpire;

    private Long refreshExpire;

    private String tokenHeader;

    private String tokenStartWith;

    private String secret;

}
