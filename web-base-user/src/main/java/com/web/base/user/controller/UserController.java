package com.web.base.user.controller;

import com.google.common.base.Preconditions;
import com.web.base.boot.auth.JwtUtils;
import com.web.base.boot.auth.TokenProperties;
import com.web.base.boot.auth.Uid;
import com.web.base.common.base.BusinessException;
import com.web.base.common.web.WebResponse;
import com.web.base.persist.auth.entity.SystemUser;
import com.web.base.persist.auth.repository.SystemUserRepository;
import com.web.base.user.domain.TokenResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private SystemUserRepository userRepository;

    @ApiOperation(value = "添加账号")
    @PostMapping(value = "add-account")
    public WebResponse<?> addAccount(String account, String password) {

        Preconditions.checkNotNull(account, "account can not be null");
        Preconditions.checkNotNull(password, "password can not be null");

        if (userRepository.findOneByAccount(account) != null) {
            throw new BusinessException(1, "账号已存在");
        }

        SystemUser user = new SystemUser();
        user.setAccount(account);
        user.setPassword(DigestUtils.sha1Hex(password));
        userRepository.save(user);
        return WebResponse.success("SUCCESS");
    }

    /**
     * 登录
     *
     * @param account
     * @param password
     * @return
     */
    @ApiOperation(value = "登录", notes = "code = 1 账号或密码错误")
    @PostMapping(value = "login")
    public WebResponse<TokenResponse> login(String account, String password) {
        log.info("password:{}", DigestUtils.sha1Hex(password));
        SystemUser systemUser = userRepository.findOneByAccountAndPassword(account, DigestUtils.sha1Hex(password));
        if (systemUser == null) {
            return WebResponse.fail(1, "账号或密码错误");
        }
        String token = JwtUtils.generateToken(systemUser.getId(), tokenProperties.getTokenExpire(), tokenProperties.getSecret());
        String refreshToken = JwtUtils.generateToken(systemUser.getId(), tokenProperties.getRefreshExpire(), tokenProperties.getSecret());
        TokenResponse response = new TokenResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        return WebResponse.success(response);
    }

    @ApiOperation(value = "刷新token", notes = "http code=401 token过期或者无效")
    @PostMapping("refresh-token")
    public WebResponse<TokenResponse> refreshToken(@Uid Long uid) {
        String token = JwtUtils.generateToken(uid, tokenProperties.getTokenExpire(), tokenProperties.getSecret());
        String refreshToken = JwtUtils.generateToken(uid, tokenProperties.getRefreshExpire(), tokenProperties.getSecret());
        TokenResponse response = new TokenResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        return WebResponse.success(response);
    }

}
