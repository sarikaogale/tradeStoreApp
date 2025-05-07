package com.datastore.trade.config;


import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.datastore.trade.command.repository", // your JPA repository package
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class MySqlDbConfig {

    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.datastore.trade.command.model");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        em.setJpaPropertyMap(hibernateProperties());
        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, Object> hibernateProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto", "update"));
        props.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"));
        props.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql", "true"));
        return props;
    }



}

