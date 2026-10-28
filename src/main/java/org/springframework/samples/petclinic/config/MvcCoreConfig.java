package org.springframework.samples.petclinic.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.web.PetTypeFormatter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.xml.MarshallingView;

/**
 * Java-based replacement of {@code mvc-core-config.xml} and {@code mvc-view-config.xml}:
 * DispatcherServlet application context for PetClinic's web tier.
 */
@Configuration
@ComponentScan(basePackages = "org.springframework.samples.petclinic.web")
public class MvcCoreConfig extends WebMvcConfigurationSupport {

    @Autowired
    private ClinicService clinicService;

    /** All resources inside folder src/main/webapp/resources are mapped so they can be referred to inside JSP files. */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        // uses WebJars so Javascript and CSS libs can be declared as Maven dependencies (Bootstrap, jQuery...)
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("welcome");
    }

    /** Serve static resources (*.html, ...) from src/main/webapp/. */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * The ContentNegotiatingViewResolver delegates to the InternalResourceViewResolver and BeanNameViewResolver,
     * and uses the requested media type to pick a matching view.
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(true, new JstlView(""));
        registry.beanName();
        registry.jsp("/WEB-INF/jsp/", ".jsp");
    }

    /** Conversion service with the formatter that parses and prints elements of type PetType. */
    @Bean
    public FormattingConversionServiceFactoryBean conversionService() {
        FormattingConversionServiceFactoryBean factoryBean = new FormattingConversionServiceFactoryBean();
        factoryBean.setFormatters(Collections.singleton(new PetTypeFormatter(clinicService)));
        return factoryBean;
    }

    /** The MVC infrastructure uses the 'conversionService' bean above. */
    @Override
    @Bean
    public FormattingConversionService mvcConversionService() {
        return conversionService().getObject();
    }

    /** Message source for this context, loaded from localized "messages_xx" files stored in src/main/resources. */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        return messageSource;
    }

    /** Resolves specific types of exceptions to corresponding logical view names for error views. */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        resolver.setDefaultErrorView("exception");
        // needed otherwise exceptions won't be logged anywhere
        resolver.setWarnLogCategory("warn");
        return resolver;
    }

    /** Renders an XML view. Used by the BeanNameViewResolver. */
    @Bean(name = "vets/vetList.xml")
    public MarshallingView vetListXmlView(Jaxb2Marshaller marshaller) {
        return new MarshallingView(marshaller);
    }

    /** Object-XML mapping declared using annotations inside 'Vets'. */
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Vets.class);
        return marshaller;
    }
}
