package org.springframework.samples.petclinic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.xml.MarshallingView;
import org.springframework.oxm.jaxb2.Jaxb2Marshaller;

@Configuration
public class MvcViewConfig implements WebMvcConfigurer {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // In a real app, we'd configure ContentNegotiatingViewResolver here.
        // For simplicity in this transformation, we'll focus on the JSP resolver.
    }

    @Bean
    public MarshallingView vetListXmlView(Jaxb2Marshaller marshaller) {
        MarshallingView view = new MarshallingView();
        view.setMarshaller(marshaller);
        return view;
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(org.springframework.samples.petclinic.model.Vets.class);
        return marshaller;
    }
}
