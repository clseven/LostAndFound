package com.lostfound;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
@MapperScan("com.lostfound.mapper")  // 扫描MyBatis-Plus的Mapper接口（必须指定Mapper包路径）
@EnableOpenApi  // 开启Knife4j接口文档（Swagger）支持
public class LossAndFoundApplication {
    public static void main(String[] args) {
        SpringApplication.run(LossAndFoundApplication.class, args);
        log.info("server started");
    }
}
