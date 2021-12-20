package com.example.sgsparks.encoder;


import org.mindrot.jbcrypt.BCrypt;

public class BcryptPasswordEncoder implements PasswordEncoder{
    @Override
    public String encrypt(String password) {
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }

    @Override
    public boolean isMatch(String password, String hashed) {
        return BCrypt.checkpw(password,hashed);
    }
}
