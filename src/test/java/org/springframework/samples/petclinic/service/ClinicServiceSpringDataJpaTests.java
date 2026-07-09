package org.springframework.samples.petclinic.service;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.samples.petclinic.BusinessConfig;
import org.springframework.samples.petclinic.DataSourceConfig;

/**
 * <p> Integration test using the 'Spring Data' profile.
 *
 * @author Michael Isvy
 * @see AbstractClinicServiceTests AbstractClinicServiceTests for more details. </p>
 */

@SpringJUnitConfig(classes = {BusinessConfig.class, DataSourceConfig.class})
@ActiveProfiles("spring-data-jpa")
class ClinicServiceSpringDataJpaTests extends AbstractClinicServiceTests {

}
