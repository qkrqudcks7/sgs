package com.example.sgsparks.service;

import com.example.sgsparks.dto.request.*;
import com.example.sgsparks.dto.response.MyToken;
import com.example.sgsparks.dto.response.UserResponse;
import com.example.sgsparks.encoder.PasswordEncoder;
import com.example.sgsparks.entity.Role;
import com.example.sgsparks.entity.User;
import com.example.sgsparks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${token.refresh_time}")
    private Long REFRESH_TIME;

    public String getMessage() {
        return "메세지 반환";
    }

    @Transactional
    public String signUp(SignUpRequest signUpRequest) {
        userRepository.save(User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encrypt(signUpRequest.getPassword()))
                .role(Role.ROLE_USER)
                .name(signUpRequest.getName()).build());

        return signUpRequest.getName() +"님 회원가입 되었습니다.";
    }

    @Transactional
    public ResponseEntity<MyToken> login(SignInRequest signInRequest) {
        User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("없는 계정입니다."));

        if (!passwordEncoder.isMatch(signInRequest.getPassword(),user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String refresh = (String) redisTemplate.opsForValue().get(user.getEmail());

        if (refresh == null || !securityService.isValid(refresh)) {
            String token = securityService.createToken(user.getEmail());
            String refreshToken = securityService.createRefreshToken(user.getEmail());
            redisTemplate.opsForValue().set(user.getEmail(), refreshToken,REFRESH_TIME, TimeUnit.MILLISECONDS);

            MyToken myToken = MyToken.builder()
                    .message("로그인 성공")
                    .email(user.getEmail())
                    .name(user.getName())
                    .accessToken(token)
                    .refreshToken(refreshToken)
                    .refreshTokenExpirationTime(REFRESH_TIME)
                    .role(user.getRole().toString()).build();
            return ResponseEntity.status(HttpStatus.OK).body(myToken);
        } else {
            String token = securityService.createToken(user.getEmail());
            MyToken myToken = MyToken.builder()
                    .message("로그인 성공")
                    .email(user.getEmail())
                    .name(user.getName())
                    .accessToken(token)
                    .refreshToken(refresh)
                    .refreshTokenExpirationTime(REFRESH_TIME)
                    .role(user.getRole().toString())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(myToken);
        }
    }

    public String sendVerificationMail(EmailRequest emailRequest) {
        if (emailRequest.getEmail() == null) {
            throw new NullPointerException("유저가 없음");
        }
        UUID uuid = UUID.randomUUID();
        redisTemplate.opsForValue().set(uuid.toString(),emailRequest.getEmail(),60*30L,TimeUnit.SECONDS);
        emailService.sendMail(emailRequest.getEmail(),"[ SGS-Parks ] 회웝가입 인증이메일","다음 인증 코드를 입력해주세요.  " + uuid.toString());
        return uuid.toString();
    }

    public void checkEmail(String key) {
        String email = (String) redisTemplate.opsForValue().get(key);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("만료되었습니다."));
        redisTemplate.delete(key);
    }

    public String sendVerificationPassword(EmailRequest emailRequest) {
        User user = userRepository.findByEmail(emailRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("없는 이메일입니다."));
        String URL = "http://localhost:8080/checkemail/";
        UUID uuid = UUID.randomUUID();
        redisTemplate.opsForValue().set(uuid.toString(),user.getEmail(),60*30L,TimeUnit.SECONDS);
        emailService.sendMail(user.getEmail(),"[ SGS-Parks ] 회웝가입 인증이메일",URL+uuid.toString());

        return uuid.toString();
    }

    @Transactional
    public void changePassword(PasswordRequest passwordRequest) {
        User user = userRepository.findByEmail(passwordRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("없는 이메일입니다."));
        user.changePassword(passwordEncoder.encrypt(passwordRequest.getPassword()));
        userRepository.save(user);
    }

    public boolean doubleCheckEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public ResponseEntity<?> getAllUsers() {
        List<User> all = userRepository.findAll();
        List<UserResponse> collect = all.stream().map(
                user -> new UserResponse(user.getEmail(), user.getRole().toString(), user.getName())
        ).collect(Collectors.toList());

        return new ResponseEntity<>(collect,HttpStatus.OK);
    }

    @Transactional
    public void changeRole(RoleRequest roleRequest) {
        User user = userRepository.findByEmail(roleRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("없는 이메일입니다."));
        user.changeRole(Role.valueOf(roleRequest.getRole()));
        userRepository.save(user);
    }
}
