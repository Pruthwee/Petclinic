package org.springframework.samples.petclinic;

import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "org.springframework.samples.petclinic.service")
@EnableTransactionManagement
public class BusinessConfig {

    @Autowired
    public DataSource dataSource() {
        return null; // This will be provided by DataSourceConfig
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Configuration
    @Profile({"jpa", "spring-data-jpa"})
    public static class JpaConfig {
        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(dataSource);
            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            // These should be externalized in a real scenario, but for now we keep them as placeholders
            // or use @Value if we had a properties file loaded.
            em.setJpaVendorAdapter(vendorAdapter);
            em.setPersistenceUnitName("petclinic");
            em.setPackagesToScan("org.springframework.samples.petclinic");
            return em;
        }

        @Bean
        public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory.getObject());
        }
    }

    @Configuration
    @Profile("jdbc")
    public static class JdbcConfig {
        @Bean
        public DataSourceTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
            return new NamedParameterJdbcTemplate(dataSource);
        }
    }
}
