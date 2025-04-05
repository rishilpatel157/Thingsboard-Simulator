package com.devicesimulator.thingsboard_device_simulator.service;

import com.devicesimulator.thingsboard_device_simulator.config.ThingsboardConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.DeviceInfo;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.User;
import org.thingsboard.server.common.data.alarm.Alarm;
import org.thingsboard.server.common.data.alarm.AlarmInfo;
import org.thingsboard.server.common.data.id.AlarmId;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.page.PageData;
import org.thingsboard.server.common.data.page.PageLink;
import org.thingsboard.server.common.data.page.TimePageLink;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceServiceImpl implements DeviceService{


  private final  RestClient tenantRestClient;
   private final RestTemplate restTemplate;
    private final AuthService authService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public DeviceServiceImpl(@Qualifier("tenantRestClient") RestClient tenantRestClient, RestTemplate restTemplate, AuthService authService) {
        this.tenantRestClient = tenantRestClient;
        this.restTemplate = restTemplate;
        this.authService = authService;
    }

    @Override
    public List<Device> getAllDevices() {

        List<Device> devices = new ArrayList<>();



        PageData<Device> pageData;
    try {
        PageLink pageLink = new PageLink(10);
        User user = tenantRestClient.getUser().orElseThrow(() -> new IllegalArgumentException("No logged-in user found"));

        System.out.println(user);
        do {
           pageData = tenantRestClient.getTenantDevices("",pageLink);

               devices.addAll(pageData.getData());
               pageLink = pageLink.nextPageLink();
        } while (pageData.hasNext());
    }
     catch(Exception e){
            throw new IllegalArgumentException("Failed to fetch devices", e);
        } finally{
            tenantRestClient.logout();
            tenantRestClient.close();
        }
        for(Device i : devices){
            System.out.println(i.getDeviceProfileId());
        }
        return devices;
    }


    public JsonNode getDeviceHealthStatus(String deviceIdStr) {
        String token = authService.getJwtToken();
        UUID deviceUUID = UUID.fromString(deviceIdStr);
        DeviceId deviceId = new DeviceId(deviceUUID);

        // Get device info
        Optional<Device> optionalDevice = tenantRestClient.getDeviceById(deviceId);
        if (optionalDevice.isEmpty()) {
            throw new RuntimeException("Device not found for id: " + deviceIdStr);
        }

        Device device = optionalDevice.get();
        String deviceName = device.getName();
        String deviceType = device.getType();

        // Get active alarms
        String alarmUrl = ThingsboardConfig.BASE_URL + "/alarm/DEVICE/" + deviceIdStr +
                "?pageSize=10&page=0&sortOrder=DESC&searchStatus=ACTIVE";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                alarmUrl, HttpMethod.GET, request, JsonNode.class
        );

        boolean hasActiveAlarms = response.getBody() != null
                && response.getBody().has("data")
                && response.getBody().get("data").isArray()
                && response.getBody().get("data").size() > 0;

        String healthStatus = hasActiveAlarms ? "UNHEALTHY" : "HEALTHY";

        // Build JSON response
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("deviceName", deviceName);
        result.put("deviceType", deviceType);
        result.put("healthStatus", healthStatus);

        return result;
    }



    public JsonNode getActiveAlarmsByDeviceId(String deviceId) {
        String token = authService.getJwtToken();

        String url = ThingsboardConfig.BASE_URL + "/alarm/DEVICE/" + deviceId
                + "?pageSize=10&page=0&sortOrder=DESC&searchStatus=ACTIVE";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, JsonNode.class);

            JsonNode responseBody = response.getBody();

            if (responseBody != null && responseBody.has("data") && responseBody.get("data").isArray()) {
                if (responseBody.get("data").size() == 0) {
                    System.out.println("No active alarms found for device: " + deviceId);
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.createObjectNode().put("message", "No active alarms found for device: " + deviceId);
                } else {
                    return responseBody;
                }
            } else {
                System.out.println("Unexpected response structure or null body for device: " + deviceId);
                ObjectMapper mapper = new ObjectMapper();
                return mapper.createObjectNode().put("message", "No active alarms found or invalid response.");
            }

        } catch (Exception e) {
            System.err.println("Error retrieving alarms for device " + deviceId + ": " + e.getMessage());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.createObjectNode().put("error", "Failed to fetch alarms: " + e.getMessage());
        }
    }

}
