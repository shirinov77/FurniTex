package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Bu sinf Spring MVC da JPA (Hibernate) konfiguratsiyasini boshqaradi.
 * Ma'lumotlar bazasiga ulanish va entity sinflarini skanerlash uchun javobgar.
 */
@Configuration
@EnableTransactionManagement
public class JpaConfiguration {

    @Autowired
    private Environment environment;

    /**
     * Ma'lumotlar bazasiga ulanish uchun DataSource obyektini sozlaydi.
     * Ma'lumotlar application.properties faylidan olinadi.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
        return dataSource;
    }

    /**
     * JPA (Java Persistence API) uchun EntityManagerFactory yaratadi.
     * Bu JPA ning asosiy obyektidir va ma'lumotlar bazasi bilan ishlash uchun kerak.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("org.example.model"); // JPA ga entity sinflari joylashgan paketni ko'rsatamiz

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());

        return em;
    }

    /**
     * Hibernate uchun qo'shimcha xususiyatlarni belgilaydi.
     * Eng muhimi, "hibernate.hbm2ddl.auto" jadvallarni avtomatik yaratish uchun javobgar.
     */
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", environment.getRequiredProperty("spring.jpa.hibernate.ddl-auto"));
        properties.setProperty("hibernate.dialect", environment.getRequiredProperty("spring.jpa.database-platform"));
        properties.setProperty("hibernate.show_sql", environment.getRequiredProperty("spring.jpa.show-sql"));
        properties.setProperty("hibernate.format_sql", environment.getRequiredProperty("spring.jpa.properties.hibernate.format_sql"));
        return properties;
    }

    /**
     * Tranzaksiyalarni boshqarish uchun JpaTransactionManager yaratadi.
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}