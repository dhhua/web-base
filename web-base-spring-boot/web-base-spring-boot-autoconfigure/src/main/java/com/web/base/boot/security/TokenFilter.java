package com.web.base.boot.security;

import com.web.base.boot.auth.JwtUtils;
import com.web.base.boot.auth.TokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author /
 */
@Slf4j
public class TokenFilter extends GenericFilterBean {

    private TokenProperties tokenProperties;

    public void setTokenProperties(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);
        String requestRri = httpServletRequest.getRequestURI();
        // 验证 token 是否存在
        if (StringUtils.hasText(token)) {
            try {
                Authentication authentication = JwtUtils.getAuthentication(token, tokenProperties.getSecret());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("set Authentication to security context for '{}', uri: {}", authentication.getName(), requestRri);
            } catch (Exception e) {
                logger.info("failed to valid token", e);
            }
        } else {
            log.debug("no valid JWT token found, uri: {}", requestRri);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(tokenProperties.getTokenHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenProperties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(tokenProperties.getTokenStartWith(), "");
        }
        return null;
    }
}
