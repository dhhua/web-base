package com.web.base.boot.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author dong
 * @date 2020/5/1
 */
public class JwtUtils {

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

    public static Claims getClaimByToken(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean isTokenExpired(Date date) {
        return new Date().after(date);
    }
}
