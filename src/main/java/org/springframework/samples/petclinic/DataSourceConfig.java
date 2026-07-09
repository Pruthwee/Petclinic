package org.springframework.samples.petclinic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.core.io.ClassPathResource;
import org.apache.tomcat.jdbc.pool.DataSource;

import javax.sql.DataSource as javaxDataSource;

@Configuration
@PropertySource("classpath:spring/data-access.properties")
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        DataSource ds = new DataSource();
        ds.setDriverClassName(System.getProperty("jdbc.driverClassName", "org.hsqldb.jdbc.JDBCDriver"));
        ds.setUrl(System.getProperty("jdbc.url", "jdbc:hsqldb:mem:petclinic"));
        ds.setUsername(System.getProperty("jdbc.username", "sa"));
        ds.setPassword(System.getProperty("jdbc.password", ""));
        return ds;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource(System.getProperty("jdbc.initLocation", "spring/schema.sql")));
        populator.addScript(new ClassPathResource(System.getProperty("jdbc.dataLocation", "spring/data.sql")));
        
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
