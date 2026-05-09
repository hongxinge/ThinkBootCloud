package com.thinkboot.gateway.model;

import lombok.Data;

@Data
public class GatewayResponse<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp;

    public static <T> GatewayResponse<T> error(int code, String message) {
        GatewayResponse<T> response = new GatewayResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
}
