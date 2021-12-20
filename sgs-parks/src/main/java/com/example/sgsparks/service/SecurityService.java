package com.example.sgsparks.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public interface SecurityService {

    String createToken(String email);

    String createRefreshToken(String email);

    Claims checkToken(String token);

    boolean isValid(String accessToken);
}
