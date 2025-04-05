package com.devicesimulator.thingsboard_device_simulator.service;

import com.devicesimulator.thingsboard_device_simulator.controller.DeviceController;
import com.devicesimulator.thingsboard_device_simulator.enums.DeviceType;
import com.devicesimulator.thingsboard_device_simulator.generator.DataGenerator;
import com.devicesimulator.thingsboard_device_simulator.generator.DataGeneratorFactory;
import com.devicesimulator.thingsboard_device_simulator.generator.PowerDataGenerator;
import com.devicesimulator.thingsboard_device_simulator.generator.TemperatureDataGenerator;
import com.devicesimulator.thingsboard_device_simulator.utils.MqttPublisher;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Service
public class DeviceGeneratorService {
    private  MqttPublisher mqttPublisher;
    private static final Logger log = LoggerFactory.getLogger(DeviceGeneratorService.class);

    private final Map<String, Boolean> deviceRunningMap = new ConcurrentHashMap<>();

    private volatile boolean isRunning = false; // Flag to control execution

    private static final Map<String, String> DEVICE_TOKENS = Map.of(
            "temperature", "y28kNhk62nsIGefizbuk",
            "power", "KShNjtmkjwcccSw943tD"
    );

    public DeviceGeneratorService(MqttPublisher mqttPublisher) {
        this.mqttPublisher = mqttPublisher;
    }


    public void sendData(String deviceType) {
        try {
            DeviceType type = DeviceType.fromString(deviceType);
            DataGenerator generator = DataGeneratorFactory.getGenerator(type);

            String data = generator.generateData();

            String accessToken = DEVICE_TOKENS.get(deviceType);

            if (accessToken == null) {
                return;
            }
            System.out.println("data" +data);
          mqttPublisher.publish(deviceType, data, accessToken);
            Thread.sleep(1000);
        } catch (Exception e) {

            System.out.println("error" + e.getMessage());
        }
    }

    @Async
    public void startSendingData(String deviceType) {
        isRunning = true; // Enable the loop
        deviceRunningMap.put(deviceType, true);
        log.info("Started simulation for device: {}", deviceType);

        while (Boolean.TRUE.equals(deviceRunningMap.get(deviceType))) {
            sendData(deviceType);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Thread interrupted for deviceType {}", deviceType);
            }
        }

        log.info("Stopped simulation for device: {}", deviceType);
    }

    public void stopSendingData() {
        isRunning = false; // Stop the loop

    }

}
