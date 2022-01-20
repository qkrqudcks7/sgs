package com.example.sgsparks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordRequest {

    private String email;
    private String password;
}
