package top.rectorlee.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.rectorlee.utils.DataSourceProperties;
import top.rectorlee.utils.DynamicDataSourceFactory;

import javax.sql.DataSource;

/**
 * @description: 数据源配置类
 * @author: Lee
 * @date: 2023-07-24 20:21:25
 * @version: 1.0
 */
@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dynamicDataSource(DataSourceProperties dataSourceProperties) {
        return DynamicDataSourceFactory.buildDruidDataSource(dataSourceProperties);
    }
}
