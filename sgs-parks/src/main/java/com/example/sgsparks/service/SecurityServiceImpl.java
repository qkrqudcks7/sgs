package com.example.sgsparks.service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Value("${token.secret_key}")
    private String SECRET_KEY;

    @Value("${token.time}")
    private Long TIME;

    @Value("${token.refresh_time}")
    private Long REFRESH_TIME;

    @Override
    public String createToken(String email) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] bytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setSubject(email)
                .signWith(signatureAlgorithm, secretKeySpec)
                .setExpiration(new Date(System.currentTimeMillis()+ TIME))
                .compact();
    }

    @Override
    public String createRefreshToken(String email) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] bytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setSubject(email)
                .signWith(signatureAlgorithm, secretKeySpec)
                .setExpiration(new Date(System.currentTimeMillis()+ REFRESH_TIME))
                .compact();
    }

    @Override
    public Claims checkToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public boolean isValid(String accessToken) {
        try {
            checkToken(accessToken);
            return true;
        } catch (ExpiredJwtException exception) {
            System.out.println("토큰이 만료됐습니다 - " +exception.getClaims().getSubject());
            return false;
        } catch (JwtException exception) {
            System.out.println("토큰이 변조 되었습니다.");
            return false;
        } catch (NullPointerException exception) {
            System.out.println("토큰이 없습니다.");
            return false;
        }
    }
}
