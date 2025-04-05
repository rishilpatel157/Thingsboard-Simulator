package com.devicesimulator.thingsboard_device_simulator.generator;

import com.devicesimulator.thingsboard_device_simulator.enums.DeviceType;

import static com.devicesimulator.thingsboard_device_simulator.enums.DeviceType.POWER;
import static com.devicesimulator.thingsboard_device_simulator.enums.DeviceType.TEMPERATURE;

public class DataGeneratorFactory {
    public static DataGenerator getGenerator(DeviceType type) {
        return switch (type) {
            case TEMPERATURE -> new TemperatureDataGenerator();
            case POWER -> new PowerDataGenerator();
        };
    }
}
