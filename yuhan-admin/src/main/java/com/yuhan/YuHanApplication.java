package com.yuhan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Author jzx
 * @Date 2021/2/2 11:13
 * @Version 1.0
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class YuHanApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuHanApplication.class,args);
        System.out.println("########  项目启动成功！  ########");
    }
}
