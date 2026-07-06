package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.config.BusinessConfig;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * <p>Integration test using the jpa profile.</p>
 *
 * <p>Migrated from XML-based configuration:
 * {@code @SpringJUnitConfig(locations = {"classpath:spring/business-config.xml", ...})}
 * to annotation-driven Java configuration:
 * {@code @SpringJUnitConfig(classes = {BusinessConfig.class})}
 * for cloud-native compatibility with AWS environments.</p>
 *
 * <p>Environment-specific values (database URL, credentials) are externalized via
 * environment variables or AWS Parameter Store / Secrets Manager, following
 * 12-factor app principles.</p>
 *
 * @author Rod Johnson
 * @author Sam Brannen
 * @author Michael Isvy
 * @see AbstractClinicServiceTests AbstractClinicServiceTests for more details.
 */
@SpringJUnitConfig(classes = {BusinessConfig.class})
@ActiveProfiles("jpa")
class ClinicServiceJpaTests extends AbstractClinicServiceTests {

}
