package com.lzx.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@SpringBootApplication(scanBasePackages = "com.lzx")
@MapperScan("com.lzx.domain.mapper")

public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
