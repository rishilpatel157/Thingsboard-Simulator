package com.devicesimulator.thingsboard_device_simulator.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.thingsboard.server.common.data.Device;

import java.util.List;

public interface DeviceService {

    public List<Device> getAllDevices();
    public JsonNode getActiveAlarmsByDeviceId(String deviceId);
    public JsonNode getDeviceHealthStatus(String deviceId);
}
