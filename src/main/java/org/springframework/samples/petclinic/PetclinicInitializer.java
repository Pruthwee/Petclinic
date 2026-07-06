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
package org.springframework.samples.petclinic;

import org.springframework.samples.petclinic.config.BusinessConfig;
import org.springframework.samples.petclinic.config.MvcConfig;
import org.springframework.samples.petclinic.config.ToolsConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;


/**
 * In Servlet 3.0+ environments, this class replaces the traditional {@code web.xml}-based approach in order to configure the
 * {@link ServletContext} programmatically.
 * <p/>
 * Create the Spring "<strong>root</strong>" application context.<br/>
 * Register a {@link DispatcherServlet}  in the servlet context.<br/>
 * For both servlets, register a {@link CharacterEncodingFilter}.
 * <p/>
 * Migrated from XML-based configuration (classpath:spring/business-config.xml,
 * classpath:spring/tools-config.xml, classpath:spring/mvc-core-config.xml) to
 * annotation-driven Java configuration using {@link AnnotationConfigWebApplicationContext}.
 * Environment-specific values are externalized to AWS Parameter Store, Secrets Manager,
 * or environment variables following 12-factor app principles.
 *
 * @author Antoine Rey
 */
public class PetclinicInitializer extends AbstractDispatcherServletInitializer {

    /**
     * Spring profile used to choose the persistence layer implementation.
     * <p>
     * When using Spring jpa, use: jpa
     * When using Spring JDBC, use: jdbc
     * When using Spring Data JPA, use: spring-data-jpa
     * <p/>
     * <p>
     * You also may use the -Dspring.profiles.active=jdbc VM options or the
     * SPRING_PROFILES_ACTIVE environment variable to change the default jpa Spring profile.
     * In AWS environments, set the SPRING_PROFILES_ACTIVE environment variable via
     * ECS task definition, Elastic Beanstalk environment properties, or AWS Parameter Store.
     */
    private static final String DEFAULT_SPRING_PROFILE = "jpa";

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        // Migrated from XmlWebApplicationContext (classpath:spring/business-config.xml,
        // classpath:spring/tools-config.xml) to AnnotationConfigWebApplicationContext
        // to support annotation-driven Java configuration (cloud-native pattern).
        // Environment-specific values are externalized via AWS Parameter Store or
        // environment variables (SPRING_PROFILES_ACTIVE).
        AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
        rootAppContext.register(BusinessConfig.class, ToolsConfig.class);
        // Read active profile from environment variable for cloud-native configuration;
        // falls back to DEFAULT_SPRING_PROFILE if not set.
        String activeProfile = System.getenv("SPRING_PROFILES_ACTIVE");
        if (activeProfile != null && !activeProfile.trim().isEmpty()) {
            rootAppContext.getEnvironment().setActiveProfiles(activeProfile.trim());
        } else {
            rootAppContext.getEnvironment().setDefaultProfiles(DEFAULT_SPRING_PROFILE);
        }
        return rootAppContext;
    }

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        // Migrated from XmlWebApplicationContext (classpath:spring/mvc-core-config.xml)
        // to AnnotationConfigWebApplicationContext to support annotation-driven Java
        // configuration (cloud-native pattern).
        AnnotationConfigWebApplicationContext webAppContext = new AnnotationConfigWebApplicationContext();
        webAppContext.register(MvcConfig.class);
        return webAppContext;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        // Used to provide the ability to enter Chinese characters inside the Owner Form
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("UTF-8", true);
        return new Filter[]{characterEncodingFilter};
    }

}
