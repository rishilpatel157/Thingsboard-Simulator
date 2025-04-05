package com.devicesimulator.thingsboard_device_simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thingsboard.rest.client.RestClient;


@SpringBootApplication
@EnableScheduling
public class ThingsboardDeviceSimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThingsboardDeviceSimulatorApplication.class, args);

	}

}
