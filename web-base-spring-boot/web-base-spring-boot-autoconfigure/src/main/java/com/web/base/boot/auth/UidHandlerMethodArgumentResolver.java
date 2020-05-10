package com.web.base.boot.auth;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class UidHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private TokenProperties tokenProperties;

    private static final String DEFAULT_TOKEN_HEADER = "Authorization";

    public UidHandlerMethodArgumentResolver(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    public UidHandlerMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(Uid.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new RuntimeException("request is null");
        }
        if (tokenProperties == null) {
            throw new RuntimeException("token config error");
        }
        String tokenHeaderName = StringUtils.isEmpty(tokenProperties.getTokenHeader()) ? DEFAULT_TOKEN_HEADER : tokenProperties.getTokenHeader();
        String tokenString = request.getHeader(tokenHeaderName);
        String token = JwtUtils.resolveToken(tokenString, tokenProperties.getTokenStartWith());
        Uid uidAnnotation = methodParameter.getParameterAnnotation(Uid.class);
        if (uidAnnotation == null) {
            throw new RuntimeException("uid annotation should not be null");
        }

        if (StringUtils.isEmpty(token)) {
            if (uidAnnotation.require()) {
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
            } else {
                return 0L;
            }
        }
        JwtUtils.getClaimByToken(token, tokenProperties.getSecret());
        try {
            Claims claims = JwtUtils.getClaimByToken(token, tokenProperties.getSecret());
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            log.warn("failed to get claim by token", e);
            if (uidAnnotation.require()) {
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
            }
            return 0L;
        }
    }
}
