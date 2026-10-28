package org.springframework.samples.petclinic.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Java-based replacement of {@code business-config.xml}: repository and service layers.
 * <p>
 * Three profiles to choose from: jdbc (Spring's JdbcTemplate), jpa and spring-data-jpa.
 */
@Configuration
@Import(DataSourceConfig.class)
@ComponentScan(basePackages = "org.springframework.samples.petclinic.service")
@EnableTransactionManagement
public class BusinessConfig {

    @Configuration
    @Profile({"jpa", "spring-data-jpa"})
    public static class JpaConfig {

        @Autowired
        private Environment env;

        /** JPA EntityManagerFactory. */
        @Bean
        @DependsOn("dataSourceInitializer")
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            vendorAdapter.setDatabase(Database.valueOf(env.getRequiredProperty("jpa.database")));
            vendorAdapter.setShowSql(env.getRequiredProperty("jpa.showSql", Boolean.class));

            LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
            factory.setDataSource(dataSource);
            factory.setJpaVendorAdapter(vendorAdapter);
            // BOTH Persistence Unit and Packages to Scan are NOT compatible, persistenceUnit will win
            factory.setPersistenceUnitName("petclinic");
            factory.setPackagesToScan("org.springframework.samples.petclinic");
            return factory;
        }

        /** Transaction manager for a single JPA EntityManagerFactory (alternative to JTA). */
        @Bean
        public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
        }

        /** Post-processor to perform exception translation on @Repository classes. */
        @Bean
        public static PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
            return new PersistenceExceptionTranslationPostProcessor();
        }
    }

    @Configuration
    @Profile("jdbc")
    @ComponentScan(basePackages = "org.springframework.samples.petclinic.repository.jdbc")
    public static class JdbcConfig {

        /** Transaction manager for a single JDBC DataSource (alternative to JTA). */
        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean
        @DependsOn("dataSourceInitializer")
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        @DependsOn("dataSourceInitializer")
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
            return new NamedParameterJdbcTemplate(dataSource);
        }
    }

    /**
     * Loads JPA beans. Will automatically be transactional due to @Transactional.
     * EntityManager will be auto-injected due to @PersistenceContext.
     * PersistenceExceptions will be auto-translated due to @Repository.
     */
    @Configuration
    @Profile("jpa")
    @ComponentScan(basePackages = "org.springframework.samples.petclinic.repository.jpa")
    public static class PlainJpaConfig {
    }

    @Configuration
    @Profile("spring-data-jpa")
    @EnableJpaRepositories(basePackages = "org.springframework.samples.petclinic.repository.springdatajpa")
    public static class SpringDataJpaConfig {
    }
}
