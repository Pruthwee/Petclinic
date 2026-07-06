/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

/**
 * Java-based Business (Service + Repository) layer configuration replacing business-config.xml.
 * <p>
 * Supports three Spring profiles:
 * <ul>
 *   <li>jpa          - JPA with Hibernate via JpaOwnerRepositoryImpl etc.</li>
 *   <li>jdbc         - Spring JdbcTemplate via JdbcOwnerRepositoryImpl etc.</li>
 *   <li>spring-data-jpa - Spring Data JPA repositories</li>
 * </ul>
 * </p>
 * <p>
 * Environment-specific values are externalized via environment variables or
 * AWS Parameter Store / Secrets Manager, following 12-factor app principles.
 * </p>
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "org.springframework.samples.petclinic.service")
@Import({DataSourceConfig.class, JdbcRepositoryConfig.class, JpaRepositoryConfig.class, SpringDataJpaRepositoryConfig.class})
public class BusinessConfig {

    /**
     * JPA database dialect. Resolved from environment variable or system property.
     * Defaults to H2 for local/test environments.
     */
    @Value("${jpa.database:H2}")
    private String jpaDatabase;

    /**
     * Whether to show SQL statements in logs. Defaults to true.
     */
    @Value("${jpa.showSql:true}")
    private boolean jpaShowSql;

    /**
     * Enables resolution of ${...} placeholders in @Value annotations.
     *
     * @return PropertySourcesPlaceholderConfigurer bean
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    // ==================  JPA and Spring Data JPA profiles  ===================

    /**
     * JPA EntityManagerFactory for jpa and spring-data-jpa profiles.
     *
     * @param dataSource the DataSource to use
     * @return configured LocalContainerEntityManagerFactoryBean
     */
    @Bean
    @Profile({"jpa", "spring-data-jpa"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(resolveDatabase(jpaDatabase));
        vendorAdapter.setShowSql(jpaShowSql);
        emf.setJpaVendorAdapter(vendorAdapter);

        emf.setPersistenceUnitName("petclinic");
        emf.setPackagesToScan("org.springframework.samples.petclinic");
        return emf;
    }

    /**
     * JPA Transaction Manager for jpa and spring-data-jpa profiles.
     *
     * @param entityManagerFactory the EntityManagerFactory
     * @return JpaTransactionManager bean
     */
    @Bean
    @Profile({"jpa", "spring-data-jpa"})
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    /**
     * PersistenceExceptionTranslationPostProcessor for jpa and spring-data-jpa profiles.
     * Translates native JPA exceptions to Spring's DataAccessException hierarchy.
     *
     * @return PersistenceExceptionTranslationPostProcessor bean
     */
    @Bean
    @Profile({"jpa", "spring-data-jpa"})
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    // ==================  JDBC profile  ===================

    /**
     * JDBC Transaction Manager for the jdbc profile.
     *
     * @param dataSource the DataSource
     * @return DataSourceTransactionManager bean
     */
    @Bean
    @Profile("jdbc")
    public DataSourceTransactionManager jdbcTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * JdbcTemplate for the jdbc profile.
     *
     * @param dataSource the DataSource
     * @return JdbcTemplate bean
     */
    @Bean
    @Profile("jdbc")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * NamedParameterJdbcTemplate for the jdbc profile.
     *
     * @param dataSource the DataSource
     * @return NamedParameterJdbcTemplate bean
     */
    @Bean
    @Profile("jdbc")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * Resolves the JPA Database enum from a string value.
     *
     * @param databaseName the database name string (e.g., "H2", "MYSQL", "POSTGRESQL")
     * @return the corresponding Database enum value
     */
    private Database resolveDatabase(String databaseName) {
        try {
            return Database.valueOf(databaseName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Database.DEFAULT;
        }
    }
}
