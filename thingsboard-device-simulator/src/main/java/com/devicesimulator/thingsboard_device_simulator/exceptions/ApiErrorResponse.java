package com.devicesimulator.thingsboard_device_simulator.exceptions;

public class ApiErrorResponse {
    private String message;
    private String errorCode;

    public ApiErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
