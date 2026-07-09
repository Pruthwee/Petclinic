package org.springframework.samples.petclinic.service;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * <p> Integration test using the jdbc profile.
 *
 * @author Thomas Risberg
 * @author Michael Isvy
 * @see AbstractClinicServiceTests AbstractClinicServiceTests for more details. </p>
 */

@SpringJUnitConfig(classes = {BusinessConfig.class, org.springframework.samples.petclinic.DataSourceConfig.class})
@ActiveProfiles("jdbc")
class ClinicServiceJdbcTests extends AbstractClinicServiceTests {


}
