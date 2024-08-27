package nl.andreschepers.bf_farm_simulator_9000;

import org.springframework.boot.SpringApplication;

public class TestBfFarmSimulator9000Application {

	public static void main(String[] args) {
		SpringApplication.from(BfFarmSimulator9000Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
