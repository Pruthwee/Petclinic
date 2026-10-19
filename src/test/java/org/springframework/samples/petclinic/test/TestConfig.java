package org.springframework.samples.petclinic.test;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.service.ClinicService;

/**
 * Test configuration that provides a mock ClinicService bean.
 * Replaces the XML-based Mockito.mock() factory-method which does not
 * resolve generic return types correctly in Spring 6.x.
 */
@Configuration(proxyBeanMethods = false)
public class TestConfig {

    @Bean
    public ClinicService clinicService() {
        return Mockito.mock(ClinicService.class);
    }
}
