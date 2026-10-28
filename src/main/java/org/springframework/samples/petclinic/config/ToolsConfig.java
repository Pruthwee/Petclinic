package org.springframework.samples.petclinic.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.samples.petclinic.util.CallMonitoringAspect;

/**
 * Java-based replacement of {@code tools-config.xml}: call monitoring aspect, JMX export and caching.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableMBeanExport
@EnableCaching
public class ToolsConfig {

    /** Call monitoring aspect that monitors call count and call invocation time. */
    @Bean
    public CallMonitoringAspect callMonitor() {
        return new CallMonitoringAspect();
    }

    @Bean
    public EhCacheCacheManager cacheManager(net.sf.ehcache.CacheManager ehcache) {
        return new EhCacheCacheManager(ehcache);
    }

    @Bean
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("cache/ehcache.xml"));
        return factoryBean;
    }
}
