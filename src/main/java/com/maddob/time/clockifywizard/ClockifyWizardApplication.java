package com.maddob.time.clockifywizard;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClockifyWizardApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(ClockifyWizardApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {}

}
