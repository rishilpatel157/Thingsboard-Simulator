package com.devicesimulator.thingsboard_device_simulator.generator;

import static org.thingsboard.server.common.data.StringUtils.RANDOM;

public class TemperatureDataGenerator implements DataGenerator{
    @Override
    public String generateData() {
        double temperature = getGaussianRandom(22, 5, -10, 50);
        double humidity = getGaussianRandom(50, 10, 20, 80);
        return String.format("{\"temperature\": %.2f, \"humidity\": %.2f}", temperature, humidity);
    }

    private double getGaussianRandom(double mean, double stdDev, double min, double max) {
        double value;
        do {
            value = mean + stdDev * RANDOM.nextGaussian();
        } while (value < min || value > max);
        return value;
    }
}
