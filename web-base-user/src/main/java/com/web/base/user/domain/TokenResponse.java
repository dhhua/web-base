package com.web.base.user.domain;

import lombok.Data;

/**
 * @author dong
 * @date 2020/5/10
 */
@Data
public class TokenResponse {

    private String token;

    private String refreshToken;

}
