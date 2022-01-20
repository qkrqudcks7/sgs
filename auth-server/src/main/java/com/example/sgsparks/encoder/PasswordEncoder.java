package com.example.sgsparks.encoder;

public interface PasswordEncoder {
    String encrypt(String password);

    boolean isMatch(String password, String hashed);
}