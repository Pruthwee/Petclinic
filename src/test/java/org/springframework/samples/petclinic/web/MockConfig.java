package org.springframework.samples.petclinic.web;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.service.ClinicService;

@Configuration
public class MockConfig {

    @Bean
    public ClinicService clinicService() {
        return Mockito.mock(ClinicService.class);
    }
}
