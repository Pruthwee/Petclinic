package org.springframework.samples.petclinic.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

/**
 * Java-based replacement of {@code datasource-config.xml}: the DataSource and the database initializer.
 * <p>
 * The JDBC settings are read from the Spring {@link Environment}: the keys of
 * {@code spring/data-access.properties} (jdbc.driverClassName, jdbc.url, jdbc.username, jdbc.password,
 * jdbc.initLocation, jdbc.dataLocation) keep their names, and each of them can be overridden at runtime by a
 * system property or by the matching environment variable (for example JDBC_URL), which is how a deployment
 * injects the values (e.g. from AWS Parameter Store or Secrets Manager).
 */
@Configuration
@PropertySource("classpath:spring/data-access.properties")
public class DataSourceConfig {

    @Autowired
    private Environment env;

    /** DataSource configuration for the tomcat jdbc connection pool. */
    @Bean
    @Profile("!javaee")
    public DataSource dataSource() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    /** JNDI DataSource for JEE environments. */
    @Bean
    @Profile("javaee")
    public DataSource jndiDataSource() {
        return new JndiDataSourceLookup().getDataSource("java:comp/env/jdbc/petclinic");
    }

    /** Database initializer. If any of the script fails, the initialization stops. */
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(resourceLoader.getResource(env.getRequiredProperty("jdbc.initLocation")));
        populator.addScript(resourceLoader.getResource(env.getRequiredProperty("jdbc.dataLocation")));
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
