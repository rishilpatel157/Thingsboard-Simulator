package com.devicesimulator.thingsboard_device_simulator.utils;

import com.devicesimulator.thingsboard_device_simulator.config.MqttConfig;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.eclipse.paho.client.mqttv3.*;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MqttPublisher {

    private  MqttConnectOptions mqttConnectOptions;
    private MqttClient mqttClient;

    public MqttPublisher(MqttConnectOptions mqttConnectOptions) {
        this.mqttConnectOptions = mqttConnectOptions;
    }


    public void publish(String deviceType, String payload, String accessToken) {
        MqttClient mqttClient = null;

        try {
            // Create a fresh client for each device (with unique ID)
            mqttClient = new MqttClient(MqttConfig.getMqttBrokerUrl(), MqttClient.generateClientId(), null);
            mqttConnectOptions.setUserName(accessToken);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttClient.connect(mqttConnectOptions);

            String topic = "v1/devices/me/telemetry";

            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1);

            mqttClient.publish(topic, message);
            System.out.println("Published to MQTT [" + deviceType + "]: " + payload);
        } catch (MqttException e) {
            System.err.println("MQTT Publish Failed [" + deviceType + "]: " + e.getMessage());
        } finally {
            // Clean up connection after each publish
            try {
                if (mqttClient != null && mqttClient.isConnected()) {
                    mqttClient.disconnect();
                    mqttClient.close();
                }
            } catch (MqttException e) {
                System.err.println("Error disconnecting MQTT [" + deviceType + "]: " + e.getMessage());
            }
        }
    }
}
