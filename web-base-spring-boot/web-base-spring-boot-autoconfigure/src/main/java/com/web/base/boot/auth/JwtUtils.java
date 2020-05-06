package com.web.base.boot.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author dong
 * @date 2020/5/1
 */
public class JwtUtils {

    private static final String AUTHORITIES_KEY = "auth";

    /**
     * 不需要权限控制时创建
     *
     * @param uid
     * @param expiredTime
     * @param secret
     * @return
     */
    public static String generateToken(Long uid, Long expiredTime, String secret) {
        Date nowDate = new Date();
        // 过期时间
        Date expireDate = new Date(nowDate.getTime() + expiredTime);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(uid.toString())
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String createToken(Authentication authentication, Long expiredTime, String secret) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date nowDate = new Date();
        // 过期时间
        Date expireDate = new Date(nowDate.getTime() + expiredTime);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(authentication.getPrincipal().toString())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static Authentication getAuthentication(String token, String secret) {
        Claims claims = getClaimByToken(token, secret);
        Object authoritiesStr = claims.get(AUTHORITIES_KEY);
        Collection<? extends GrantedAuthority> authorities =
                ObjectUtils.isNotEmpty(authoritiesStr) ?
                        Arrays.stream(authoritiesStr.toString().split(","))
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()) : Collections.emptyList();

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public static Claims getClaimByToken(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public static String resolveToken(String tokenString, String startWith) {
        if (StringUtils.isEmpty(tokenString) || startWith == null) {
            return tokenString;
        }
        return tokenString.replace(startWith + " ", "");
    }
}
