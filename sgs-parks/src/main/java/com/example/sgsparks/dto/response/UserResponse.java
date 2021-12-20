package com.example.sgsparks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private String email;
    private String role;
    private String name;
}
