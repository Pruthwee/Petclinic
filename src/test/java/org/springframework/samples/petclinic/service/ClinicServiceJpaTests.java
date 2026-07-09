package org.springframework.samples.petclinic.service;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.samples.petclinic.BusinessConfig;
import org.springframework.samples.petclinic.DataSourceConfig;

/**
 * <p> Integration test using the jpa profile.
 *
 * @author Rod Johnson
 * @author Sam Brannen
 * @author Michael Isvy
 * @see AbstractClinicServiceTests AbstractClinicServiceTests for more details. </p>
 */

@SpringJUnitConfig(classes = {BusinessConfig.class, DataSourceConfig.class})
@ActiveProfiles("jpa")
class ClinicServiceJpaTests extends AbstractClinicServiceTests {

}
