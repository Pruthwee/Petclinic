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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;

/**
 * Java-based DataSource configuration replacing datasource-config.xml.
 * <p>
 * Environment-specific values (JDBC URL, credentials) are externalized via
 * environment variables or AWS Parameter Store / Secrets Manager, following
 * 12-factor app principles.
 * </p>
 *
 * <p>Supported environment variables:
 * <ul>
 *   <li>JDBC_DRIVER_CLASS_NAME - JDBC driver class name</li>
 *   <li>JDBC_URL              - JDBC connection URL</li>
 *   <li>JDBC_USERNAME         - Database username</li>
 *   <li>JDBC_PASSWORD         - Database password</li>
 *   <li>JDBC_INIT_LOCATION    - Classpath location of schema SQL script</li>
 *   <li>JDBC_DATA_LOCATION    - Classpath location of data SQL script</li>
 * </ul>
 * </p>
 */
@Configuration
public class DataSourceConfig {

    /**
     * JDBC driver class name. Resolved from environment variable JDBC_DRIVER_CLASS_NAME,
     * system property jdbc.driverClassName, or the default H2 driver.
     */
    @Value("${jdbc.driverClassName:org.h2.Driver}")
    private String driverClassName;

    /**
     * JDBC URL. Resolved from environment variable JDBC_URL,
     * system property jdbc.url, or the default in-memory H2 URL.
     */
    @Value("${jdbc.url:jdbc:h2:mem:petclinic}")
    private String url;

    /**
     * Database username. Resolved from environment variable JDBC_USERNAME,
     * system property jdbc.username, or the default 'sa'.
     */
    @Value("${jdbc.username:sa}")
    private String username;

    /**
     * Database password. Resolved from environment variable JDBC_PASSWORD,
     * system property jdbc.password, or empty string.
     */
    @Value("${jdbc.password:}")
    private String password;

    /**
     * Schema SQL script location. Resolved from environment variable or system property.
     */
    @Value("${jdbc.initLocation:classpath:db/h2/schema.sql}")
    private String initLocation;

    /**
     * Data SQL script location. Resolved from environment variable or system property.
     */
    @Value("${jdbc.dataLocation:classpath:db/h2/data.sql}")
    private String dataLocation;

    /**
     * Creates the primary DataSource bean using Tomcat JDBC connection pool.
     * Connection parameters are resolved from environment variables or system properties,
     * enabling runtime configuration without redeployment (cloud-native pattern).
     *
     * @return configured DataSource
     */
    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * Initializes the database schema and data using SQL scripts.
     * Script locations are resolved from environment variables or system properties.
     *
     * @param dataSource the DataSource to initialize
     * @return DataSourceInitializer bean
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource(
            initLocation.replace("classpath:", "")));
        populator.addScript(new ClassPathResource(
            dataLocation.replace("classpath:", "")));

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
