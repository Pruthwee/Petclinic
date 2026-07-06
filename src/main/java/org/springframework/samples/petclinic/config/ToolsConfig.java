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

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.samples.petclinic.util.CallMonitoringAspect;

/**
 * Java-based Tools (AOP, JMX, Cache) configuration replacing tools-config.xml.
 * <p>
 * Enables:
 * <ul>
 *   <li>AspectJ auto-proxy for AOP (CallMonitoringAspect)</li>
 *   <li>JMX export via @ManagedResource annotations</li>
 *   <li>EhCache-based caching via @Cacheable annotations</li>
 * </ul>
 * </p>
 */
@Configuration
@EnableAspectJAutoProxy
@EnableCaching
public class ToolsConfig {

    /**
     * CallMonitoringAspect bean that monitors call count and invocation time.
     * Exposed via JMX using @ManagedResource, @ManagedAttribute, @ManagedOperation.
     *
     * @return CallMonitoringAspect bean
     */
    @Bean
    public CallMonitoringAspect callMonitor() {
        return new CallMonitoringAspect();
    }

    /**
     * MBean exporter that exposes beans annotated with @ManagedResource via JMX.
     *
     * @return AnnotationMBeanExporter bean
     */
    @Bean
    public AnnotationMBeanExporter mbeanExporter() {
        return new AnnotationMBeanExporter();
    }

    /**
     * EhCache CacheManager bean.
     * Cache configuration is loaded from classpath:cache/ehcache.xml.
     *
     * @return EhCacheCacheManager bean
     */
    @Bean
    public EhCacheCacheManager cacheManager() {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(ehcache().getObject());
        return cacheManager;
    }

    /**
     * EhCacheManagerFactoryBean that loads EhCache configuration from classpath.
     *
     * @return EhCacheManagerFactoryBean bean
     */
    @Bean
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("cache/ehcache.xml"));
        return factoryBean;
    }
}
