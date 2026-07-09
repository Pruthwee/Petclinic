package org.springframework.samples.petclinic.service;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * <p> Integration test using the jpa profile.
 *
 * @author Rod Johnson
 * @author Sam Brannen
 * @author Michael Isvy
 * @see AbstractClinicServiceTests AbstractClinicServiceTests for more details. </p>
 */

@SpringJUnitConfig(classes = {BusinessConfig.class, org.springframework.samples.petclinic.DataSourceConfig.class})
@ActiveProfiles("jpa")
class ClinicServiceJpaTests extends AbstractClinicServiceTests {

}
