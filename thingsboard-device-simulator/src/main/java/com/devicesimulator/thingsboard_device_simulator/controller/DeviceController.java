package com.devicesimulator.thingsboard_device_simulator.controller;


import com.devicesimulator.thingsboard_device_simulator.exceptions.ApiException;
import com.devicesimulator.thingsboard_device_simulator.service.AuthService;
import com.devicesimulator.thingsboard_device_simulator.service.DeviceGeneratorService;
import com.devicesimulator.thingsboard_device_simulator.service.DeviceService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.server.common.data.Device;
import java.util.List;



@Slf4j
@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

    private final DeviceService deviceService;
    private final DeviceGeneratorService deviceGeneratorService;
    private final AuthService authService;

    public DeviceController(DeviceService deviceService, DeviceGeneratorService deviceGeneratorService, AuthService authService) {
        this.deviceService = deviceService;
        this.deviceGeneratorService = deviceGeneratorService;
        this.authService = authService;
    }

    @GetMapping("/devices")
    public ResponseEntity<List<Device>> getAllDevices() {
        log.info("Fetching all devices");
        try {
            List<Device> devices = deviceService.getAllDevices();
            log.debug("Found {} devices", devices.size());
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            log.error("Failed to fetch devices", e);
            throw new ApiException("Failed to retrieve devices", "DEVICE_FETCH_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/publish/{deviceType}")
    public ResponseEntity<String> publishSingleDevice(@PathVariable String deviceType) {
        log.info("Publishing data for deviceType: {}", deviceType);
        try {
            deviceGeneratorService.startSendingData(deviceType);
            return ResponseEntity.ok("Data sent for device: " + deviceType);
        } catch (Exception e) {
            log.error("Failed to publish device data for type: {}", deviceType, e);
            throw new ApiException("Failed to publish device data", "DEVICE_PUBLISH_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/alarms/{deviceId}")
    public ResponseEntity<JsonNode> getDeviceAlarms(@PathVariable String deviceId) {
        log.info("Fetching alarms for deviceId: {}", deviceId);
        try {
            JsonNode alarms = deviceService.getActiveAlarmsByDeviceId(deviceId);
            return ResponseEntity.ok(alarms);
        } catch (Exception e) {
            log.error("Failed to fetch alarms for deviceId: {}", deviceId, e);
            throw new ApiException("Failed to retrieve alarms", "ALARM_FETCH_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/devicehealth/{deviceId}")
    public ResponseEntity<JsonNode> getDeviceHealth(@PathVariable String deviceId) {
        log.info("Checking health for deviceId: {}", deviceId);
        try {
            JsonNode health = deviceService.getDeviceHealthStatus(deviceId);
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            log.error("Failed to fetch device health for deviceId: {}", deviceId, e);
            throw new ApiException("Failed to retrieve device health", "DEVICE_HEALTH_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopDeviceSimulation() {
        log.info("Request received to stop device simulation.");
        try {
            deviceGeneratorService.stopSendingData();
            log.info("Device simulation stopped successfully.");
            return ResponseEntity.ok("Device simulation stopped.");
        } catch (Exception e) {
            log.error("Failed to stop device simulation", e);
            throw new ApiException("Failed to stop simulation", "SIM_STOP_ERROR",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}