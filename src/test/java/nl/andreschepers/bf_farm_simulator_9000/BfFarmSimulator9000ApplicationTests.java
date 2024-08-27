package nl.andreschepers.bf_farm_simulator_9000;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class BfFarmSimulator9000ApplicationTests {

	@Test
	void contextLoads() {
	}

}
