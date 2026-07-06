package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.config.MvcConfig;
import org.springframework.samples.petclinic.config.MvcTestConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link CrashController}.
 *
 * <p>Migrated from XML-based configuration:
 * {@code @SpringJUnitWebConfig(locations = {"classpath:spring/mvc-core-config.xml", ...})}
 * to annotation-driven Java configuration:
 * {@code @SpringJUnitWebConfig(classes = {MvcConfig.class, MvcTestConfig.class})}
 * for cloud-native compatibility with AWS environments.</p>
 *
 * <p>Environment-specific values are externalized via environment variables or
 * AWS Parameter Store / Secrets Manager, following 12-factor app principles.</p>
 *
 * @author Colin But
 */
@SpringJUnitWebConfig(classes = {MvcConfig.class, MvcTestConfig.class})
class CrashControllerTests {

    @Autowired
    private CrashController crashController;

    @Autowired
    private SimpleMappingExceptionResolver simpleMappingExceptionResolver;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(crashController)
            .setHandlerExceptionResolvers(simpleMappingExceptionResolver)
            .build();
    }

    @Test
    void testTriggerException() throws Exception {
        mockMvc.perform(get("/oups"))
            .andExpect(view().name("exception"))
            .andExpect(model().attributeExists("exception"))
            .andExpect(forwardedUrl("exception"))
            .andExpect(status().isOk());
    }
}
