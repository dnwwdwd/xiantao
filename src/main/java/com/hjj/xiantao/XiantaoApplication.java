package com.hjj.xiantao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hjj.xiantao.mapper")
public class XiantaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiantaoApplication.class, args);
    }
}
