package top.rectorlee.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import top.rectorlee.utils.NamedThreadFactory;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 异步线程配置类
 * @author: Lee
 * @date: 2023-07-24 20:19:25
 * @version: 1.0
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncPoolConfig {
    @Bean("poolExecutor")
    public ThreadPoolExecutor taskExecutor() {
        int i = Runtime.getRuntime().availableProcessors();
        log.info("系统最大线程数：" + i);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(i, i + 1, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<>(200), new NamedThreadFactory("excel导出线程池"));
        log.info("excel导出线程池初始化完毕");

        return threadPoolExecutor;
    }
}
