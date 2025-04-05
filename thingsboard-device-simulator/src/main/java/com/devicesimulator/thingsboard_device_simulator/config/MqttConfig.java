package com.devicesimulator.thingsboard_device_simulator.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    private static final String MQTT_BROKER_URL = "tcp://localhost:1883";

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        return options;
    }

    public static String getMqttBrokerUrl() {
        return MQTT_BROKER_URL;
    }
}
