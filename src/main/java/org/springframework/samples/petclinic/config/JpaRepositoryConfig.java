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

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Java-based configuration for the JPA repository profile.
 * <p>
 * Activates component scanning for JPA-based repository implementations,
 * replacing the XML profile bean definition in business-config.xml.
 * </p>
 */
@Configuration
@Profile("jpa")
@ComponentScan(basePackages = "org.springframework.samples.petclinic.repository.jpa")
public class JpaRepositoryConfig {
    // Component scanning activates JpaOwnerRepositoryImpl, JpaPetRepositoryImpl, etc.
}
