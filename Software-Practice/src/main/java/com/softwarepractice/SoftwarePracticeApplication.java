package com.softwarepractice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootApplication
@ImportResource(value = "classpath:spring-config.xml")
@MapperScan(basePackages = "com.softwarepractice.dao")
public class SoftwarePracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftwarePracticeApplication.class, args);
    }

}
