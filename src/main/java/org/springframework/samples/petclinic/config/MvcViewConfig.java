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
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.xml.MarshallingView;

/**
 * Java-based MVC View configuration replacing mvc-view-config.xml.
 * <p>
 * Configures:
 * <ul>
 *   <li>InternalResourceViewResolver for JSP views</li>
 *   <li>JAXB2 Marshaller for XML views</li>
 *   <li>MarshallingView for vets XML endpoint</li>
 * </ul>
 * </p>
 */
@Configuration
public class MvcViewConfig {

    /**
     * InternalResourceViewResolver that resolves logical view names to JSP files
     * under /WEB-INF/jsp/.
     *
     * @return InternalResourceViewResolver bean
     */
    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    /**
     * JAXB2 Marshaller for XML serialization of the Vets model.
     *
     * @return Jaxb2Marshaller bean
     */
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Vets.class);
        return marshaller;
    }

    /**
     * MarshallingView for rendering the vets list as XML.
     * Used by BeanNameViewResolver when the view name is "vets/vetList.xml".
     *
     * @return MarshallingView bean named "vets/vetList.xml"
     */
    @Bean(name = "vets/vetList.xml")
    public MarshallingView vetsXmlView() {
        MarshallingView view = new MarshallingView();
        view.setMarshaller(marshaller());
        return view;
    }
}
