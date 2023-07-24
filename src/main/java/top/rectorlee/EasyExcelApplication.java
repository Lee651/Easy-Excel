package top.rectorlee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @description: 启动类
 * @author: Lee
 * @date: 2023-07-24 18:40:14
 * @version: 1.0
 */
@SpringBootApplication
@MapperScan("top.rectorlee.mapper")
@EnableAsync
public class EasyExcelApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyExcelApplication.class, args);
    }
}
