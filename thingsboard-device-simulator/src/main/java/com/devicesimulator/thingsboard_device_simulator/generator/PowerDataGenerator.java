package com.devicesimulator.thingsboard_device_simulator.generator;

import java.util.Random;

public class PowerDataGenerator implements DataGenerator{
    private static final Random RANDOM = new Random();
    private double lastPowerConsumption = 500.0;  // Initial value (assumed)

    @Override
    public String generateData() {
        double voltage = getGaussianRandom(220, 10, 110, 240);
        double current = getGaussianRandom(5, 3, 0, 20);
        double powerConsumption = voltage * current;

        // Edge case: Simulating sudden power drops or spikes
        if (RANDOM.nextDouble() < 0.05) {  // 5% chance of power surge
            powerConsumption *= 1.5;
        }
        if (RANDOM.nextDouble() < 0.02) {  // 2% chance of failure (zero power)
            powerConsumption = 0;
        }

        // Trending logic: Prevents unrealistic fluctuations
        powerConsumption = smoothTrend(powerConsumption);

        return String.format("{\"voltage\": %.2f, \"current\": %.2f, \"power\": %.2f}",
                voltage, current, powerConsumption);
    }

    private double getGaussianRandom(double mean, double stdDev, double min, double max) {
        double value;
        do {
            value = mean + stdDev * RANDOM.nextGaussian();
        } while (value < min || value > max);
        return value;
    }

    private double smoothTrend(double newPower) {
        lastPowerConsumption = (lastPowerConsumption * 0.7) + (newPower * 0.3);
        return lastPowerConsumption;
    }
}
