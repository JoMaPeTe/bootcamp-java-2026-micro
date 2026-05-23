package com.programandoenjava.chekinservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ChekinServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChekinServiceApplication.class, args);
    }

}
