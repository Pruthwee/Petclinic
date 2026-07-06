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

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Java-based configuration for the Spring Data JPA repository profile.
 * <p>
 * Enables Spring Data JPA repositories in the springdatajpa package,
 * replacing the XML profile bean definition in business-config.xml.
 * </p>
 */
@Configuration
@Profile("spring-data-jpa")
@EnableJpaRepositories(basePackages = "org.springframework.samples.petclinic.repository.springdatajpa")
public class SpringDataJpaRepositoryConfig {
    // @EnableJpaRepositories activates SpringDataOwnerRepository, SpringDataPetRepository, etc.
}
