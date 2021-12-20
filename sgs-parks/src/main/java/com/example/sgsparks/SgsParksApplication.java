package com.example.sgsparks;

import com.example.sgsparks.encoder.BcryptPasswordEncoder;
import com.example.sgsparks.encoder.PasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SgsParksApplication {

    public static void main(String[] args) {
        SpringApplication.run(SgsParksApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BcryptPasswordEncoder();
    }

}
