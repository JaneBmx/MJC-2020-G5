package com.epam.esm.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.epam.esm")
@PropertySources({
        @PropertySource("classpath:dev-db.properties"),
        @PropertySource("classpath:prod-db.properties"),
        @PropertySource("classpath:env.properties"),
})
public class AppConfig {
    private static final Logger LOGGER = LogManager.getLogger(AppConfig.class);
    private final ConfigurableEnvironment env;

    @Autowired
    public AppConfig(ConfigurableEnvironment env) {
        this.env = env;
        this.env.setActiveProfiles(this.env.getProperty("env"));
        this.env.setDefaultProfiles(this.env.getProperty("env"));
    }

    @Profile("dev")
    @Bean
    public DataSource securityDataSource() {
        ComboPooledDataSource securityDataSource = new ComboPooledDataSource();
        try {
            securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
            securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
            securityDataSource.setUser(env.getProperty("jdbc.user"));
            securityDataSource.setPassword(env.getProperty("jdbc.password"));
            securityDataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("connection.pool.maxPoolSize")));
            securityDataSource.setMinPoolSize(Integer.parseInt(env.getProperty("connection.pool.minPoolSize")));
            securityDataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("connection.pool.maxIdleTime")));
            securityDataSource.setInitialPoolSize(Integer.parseInt(env.getProperty("connection.pool.initialPoolSize")));
        } catch (PropertyVetoException e) {
            LOGGER.error("Error during loading database properties", e);
        }
        LOGGER.info("Database properties has been initialized");
        return securityDataSource;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @Autowired
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
