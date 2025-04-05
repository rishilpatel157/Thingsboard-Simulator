package com.devicesimulator.thingsboard_device_simulator.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceType {

    TEMPERATURE,
    POWER;

    public static DeviceType fromString(String type) {
        return switch (type.toLowerCase()) {
            case "temperature" -> TEMPERATURE;
            case "power" -> POWER;
            default -> throw new IllegalArgumentException("Invalid device type: " + type);
        };
    }
}
