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

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.service.ClinicService;

/**
 * Java-based test configuration replacing mvc-test-config.xml.
 * <p>
 * Provides a Mockito mock of {@link ClinicService} for use in MVC layer tests,
 * following annotation-driven Java configuration patterns (cloud-native approach).
 * </p>
 */
@Configuration
public class MvcTestConfig {

    /**
     * Creates a Mockito mock of ClinicService for use in web layer tests.
     *
     * @return mocked ClinicService
     */
    @Bean
    public ClinicService clinicService() {
        return Mockito.mock(ClinicService.class);
    }
}
