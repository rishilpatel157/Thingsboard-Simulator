# 🔌 ThingsBoard Device Simulator

A production-ready **Spring Boot** application to simulate IoT devices and publish telemetry data to **ThingsBoard** using the **MQTT protocol**. The system supports multiple device types (Temperature, Power, Voltage, and Current), and demonstrates a scalable architecture using design patterns, threading, and ThingsBoard integrations.

---

## 🚀 Features

- **Spring Boot Backend**
  - Modular, lightweight RESTful service to manage simulated devices.
  
- **MQTT Communication**
  - Secure telemetry publishing using MQTT with device-specific access tokens.

- **Simulated Telemetry**
  - Randomized data generation for temperature, power, voltage, and current.
  
- **Multithreading Support**
  - Simulate and run multiple devices concurrently using asynchronous execution.
  
- **Factory Design Pattern**
  - Scalable design for adding new device types with ease.

- **ThingsBoard Integration**
  - Uses Rule Chains for alarm triggers and device health tracking.

- **Logging & Error Handling**
  - Logs request activity and publishing results for better observability.

---

## 📡 Supported Devices

- ✅ Temperature Sensor
- ✅ Power Consumption Meter


Each device sends randomized telemetry to ThingsBoard at regular intervals.

---

## 🧠 Rule Chain Integration

- Custom alarms are triggered in ThingsBoard for threshold violations.
- Logs and error messages are forwarded for monitoring and audit.
- Rule chains handle data flow and condition checks for simulated data.

---

## 🎯 API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/devices/publish/{deviceType}` | `POST` | Start publishing telemetry for a given device |
| `/api/devices/stop` | `POST` | Stop telemetry publishing |
| `/api/devices/devices` | `GET` | List all supported device types |
| `/api/devices/devicehealth/{deviceId}` | `GET` | Fetch device online/offline status |
| `/api/devices/alarms/{deviceId}` | `GET` | Retrieve alarm state for a device |

---

## 🛠️ Technologies Used

- **Java 17**
- **Spring Boot**
- **MQTT (Eclipse Paho Client)**
- **ThingsBoard CE**
- **Lombok**
- **Gradle**

---

## 🧪 How It Works

1. Each device type has a dedicated `DataGenerator` to create randomized telemetry.
2. `DeviceGeneratorService` manages publishing using `@Async` threading.
3. Telemetry is published to ThingsBoard using `MqttPublisher`.
4. ThingsBoard processes data, triggers alarms, and displays dashboard widgets.

---

## ⚙️ Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/thingsboard-device-simulator.git
   cd thingsboard-device-simulator
