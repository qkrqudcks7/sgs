package com.example.sgsparks.aop;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

@Aspect
@Component
public class SecurityAspect {

    @Value("${token.secret_key}")
    private String KEY;

    @Before("@annotation(checkToken)")
    public void Check(CheckToken checkToken) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String token = request.getHeader("token");

        if (token == null) {
            throw new IllegalArgumentException("Token is null");
        }

        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(KEY)).parseClaimsJws(token).getBody();

        if (claims == null || claims.getSubject() == null) {
            throw new IllegalArgumentException("Claims가 비어있습니다.");
        }
        System.out.println(claims.getSubject());
        System.out.println("토큰의 subject를 인증");
    }
}
