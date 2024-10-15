package com.payment.data.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@ComponentScan({"com.payment.data"})
@MapperScan("com.payment.data")
@EntityScan({"zpg.payment.data"})
@EnableTransactionManagement
public class DataAutoConfiguration {

    public DataAutoConfiguration() {
        log.info("=== 数据模块加载 ===");
    }

}
