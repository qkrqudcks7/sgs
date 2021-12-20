package com.example.sgsparks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Reissue {

    private String accessToken;

    private String refreshToken;
}
