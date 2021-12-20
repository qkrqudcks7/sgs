package com.example.sgsparks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyToken {
    private String message;

    private String email;

    private String name;

    private String accessToken;

    private String refreshToken;

    private Long refreshTokenExpirationTime;

    private String role;
}
