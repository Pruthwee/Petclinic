package org.springframework.samples.petclinic;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Lazy;

@Configuration
@EnableCaching
public class ToolsConfig {

    @Bean
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
        factory.setConfigLocation("classpath:cache/ehcache.xml");
        return factory;
    }

    @Bean
    public EhCacheCacheManager cacheManager(EhCacheManagerFactoryBean ehcache) {
        return new EhCacheCacheManager(ehcache.getObject());
    }
}
