package com.example.sgsparks.controller;

import com.example.sgsparks.aop.CheckToken;
import com.example.sgsparks.dto.request.*;
import com.example.sgsparks.dto.response.MyToken;
import com.example.sgsparks.service.SecurityService;
import com.example.sgsparks.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth-server")
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    // 연습 api
    @CheckToken
    @GetMapping("/message")
    public String getMessage() {
        return userService.getMessage();
    }

//    @GetMapping("/check")
//    public ResponseEntity checkToken(@RequestParam("token") String token) {
//        Claims claims = securityService.checkToken(token);
//
//        return ResponseEntity.status(HttpStatus.OK).body(claims);
//    }

    @PostMapping("/signin")
    public ResponseEntity<MyToken> login(@RequestBody SignInRequest signInRequest) {
        return userService.login(signInRequest);
    }

    @PostMapping("signup")
    public String signUp(@RequestBody SignUpRequest signUpRequest) {
        return userService.signUp(signUpRequest);
    }

    @GetMapping("/doublecheckemail")
    public ResponseEntity<?> doubleCheckEmail(@RequestParam("email") String email) {
        boolean check = userService.doubleCheckEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(check);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            String key = userService.sendVerificationMail(emailRequest);
            return ResponseEntity.status(HttpStatus.OK).body(key);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }
    }

    @GetMapping("/checkemail/{key}")
    public ResponseEntity<?> checkEmail(@PathVariable String key) {
        try {
            userService.checkEmail(key);
            return ResponseEntity.status(HttpStatus.OK).body("이메일 인증 성공");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증 오류");
        }
    }

    @PostMapping("/sendpassword")
    public ResponseEntity<?> sendCheckPasswordEmail(@RequestBody EmailRequest emailRequest) {
        try {
            String key = userService.sendVerificationPassword(emailRequest);
            return ResponseEntity.status(HttpStatus.OK).body(key);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }
    }

    @PutMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordRequest passwordRequest) {
        userService.changePassword(passwordRequest);

        return ResponseEntity.status(HttpStatus.OK).body("변경 완료");
    }
    @PutMapping("/changerole")
    public ResponseEntity<?> changeRole(@RequestBody RoleRequest roleRequest) {
         userService.changeRole(roleRequest);

         return ResponseEntity.status(HttpStatus.OK).body("변경 완료");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }
}
