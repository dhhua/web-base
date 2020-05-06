package com.web.base.user.controller;

import com.web.base.boot.auth.JwtUtils;
import com.web.base.boot.auth.TokenProperties;
import com.web.base.boot.auth.Uid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dong
 * @date 2020/5/3
 */
@RestController
@RequestMapping("/base/user")
@Slf4j
public class UserController {

    @Autowired
    private TokenProperties tokenProperties;

    @GetMapping("token")
    public String getToken(Long uid) {
        return JwtUtils.generateToken(uid, tokenProperties.getTokenExpire(), tokenProperties.getSecret());
    }



    @Operation(summary = "测试token", description = "测试token")
    @GetMapping("/test-token")
    public String testToken(@Parameter(content = {@Content()},name = "token") @Uid Long uid) {
        log.info("uid:{}", uid);
        return uid.toString();
    }
}
