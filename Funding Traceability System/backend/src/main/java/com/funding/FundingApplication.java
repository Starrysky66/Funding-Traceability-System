package com.funding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 助学帮扶管理系统 启动类
 */
@SpringBootApplication
@MapperScan("com.funding.mapper")
public class FundingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundingApplication.class, args);
    }
}
