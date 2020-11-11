package com.epam.esm.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.epam.esm")
@PropertySource("classpath:dev-db.properties")
public class AppConfig {
    private static final Logger LOGGER = LogManager.getLogger(AppConfig.class);
    private final Environment env;

    public AppConfig(Environment env) {
        this.env = env;
    }

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
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(securityDataSource());
    }

    @Bean
    @Autowired
    public JdbcTemplate  getJdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
