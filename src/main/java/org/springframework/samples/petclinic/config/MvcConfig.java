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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.samples.petclinic.web.PetTypeFormatter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Java-based MVC configuration replacing mvc-core-config.xml and mvc-view-config.xml.
 * <p>
 * Configures:
 * <ul>
 *   <li>Component scanning for web controllers</li>
 *   <li>Annotation-driven MVC with custom conversion service</li>
 *   <li>Static resource handlers (webapp resources and WebJars)</li>
 *   <li>View controller for welcome page</li>
 *   <li>Message source for i18n</li>
 *   <li>Exception resolver</li>
 *   <li>JSP view resolver (imported from MvcViewConfig)</li>
 * </ul>
 * </p>
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.springframework.samples.petclinic.web")
@Import(MvcViewConfig.class)
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Conversion service with custom formatters (e.g., PetTypeFormatter).
     *
     * @return FormattingConversionServiceFactoryBean bean
     */
    @Bean
    public FormattingConversionServiceFactoryBean conversionService() {
        FormattingConversionServiceFactoryBean factoryBean = new FormattingConversionServiceFactoryBean();
        Set<Object> formatters = new HashSet<>();
        formatters.add(new PetTypeFormatter(null)); // will be injected by Spring
        factoryBean.setFormatters(formatters);
        return factoryBean;
    }

    /**
     * Message source for i18n, loaded from localized "messages_xx" files.
     *
     * @return ResourceBundleMessageSource bean
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        return messageSource;
    }

    /**
     * Exception resolver that maps specific exception types to logical view names.
     *
     * @return SimpleMappingExceptionResolver bean
     */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        resolver.setDefaultErrorView("exception");
        resolver.setWarnLogCategory("warn");
        return resolver;
    }

    /**
     * Registers static resource handlers for webapp resources and WebJars.
     *
     * @param registry the ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Registers a view controller for the welcome page.
     *
     * @param registry the ViewControllerRegistry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("welcome");
    }

    /**
     * Enables default servlet handling for static resources.
     *
     * @param configurer the DefaultServletHandlerConfigurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
