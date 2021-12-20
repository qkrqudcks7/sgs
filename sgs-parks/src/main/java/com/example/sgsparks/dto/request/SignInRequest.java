package com.example.sgsparks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class SignInRequest {
    @NotNull(message = "이메일은 필수입니다.")
    @Email
    private String email;

    @NotNull(message = "패스워드는 필수입니다.")
    @Size(min = 6, message = "6자리 이상 입력하세요.")
    private String password;
}
