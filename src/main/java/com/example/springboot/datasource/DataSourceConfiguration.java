package com.example.springboot.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:postgres.properties"),
        @PropertySource("classpath:h2.properties")
})
public class DataSourceConfiguration {
    @Autowired
    private Environment env;

    @Bean
    @ConditionalOnProperty(name = "spring.profiles.active", havingValue = "prod")
    protected DataSource getDataSourceProdProfile() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(env.getProperty("postgres.db.className"));
        dataSourceBuilder.url(env.getProperty("postgres.db.url"));
        dataSourceBuilder.username(env.getProperty("postgres.db.user"));
        dataSourceBuilder.password(env.getProperty("postgres.db.password"));
        return dataSourceBuilder.build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.profiles.active", havingValue = "local")
    protected DataSource getDataSourceLocalProfile() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(env.getProperty("h2.db.className"));
        dataSourceBuilder.url(env.getProperty("h2.db.url"));
        dataSourceBuilder.username(env.getProperty("h2.db.userName"));
        dataSourceBuilder.password(env.getProperty("h2.db.password"));
        return dataSourceBuilder.build();
    }
}
