package com.thinkboot.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.thinkboot.example.mapper")
public class ThinkBootExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThinkBootExampleApplication.class, args);
        System.out.println("ThinkBoot Example Application Started Successfully");
    }
}
