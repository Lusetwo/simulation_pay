package com.example.simulation_pay.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Data
@ConfigurationProperties(prefix = "config.jwt") // 将此配置文件和yml的配置绑定
@Configuration
public class JwtConfig {

    private String secret; // 私钥
    private long expire; // 过期时间
    private String header; // 头部

    /**
     * 生成token
     * @param subject
     * @return
     */
    public String createToken (String subject){
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000); // 过期时间，单位为ms
        return Jwts.builder()
                .setHeaderParam("typ", "JWTL")
                .setSubject(subject)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    /**
     * 获取token中注册信息
     * @param token
     * @return
     */
    public Claims getTokenClaim (String token) {
        Claims claims=null;
        try {
            claims=Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return claims;
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 验证token是否过期失效
     * @param expirationTime
     * @return
     */
    public boolean isTokenExpired (Date expirationTime) {
        return expirationTime.before(new Date());
    }
    /**
     * 获取token失效时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getTokenClaim(token).getExpiration();
    }
    /**
     * 从token中获取用户信息
     */
    public String getUsernameFromToken(String token) {
        return getTokenClaim(token).getSubject();
    }
    /**
     * 获取jwt发布时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getTokenClaim(token).getIssuedAt();
    }
}
