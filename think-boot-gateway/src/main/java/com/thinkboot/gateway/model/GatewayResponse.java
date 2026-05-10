package com.thinkboot.gateway.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class GatewayResponse<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> GatewayResponse<T> success(T data) {
        GatewayResponse<T> response = new GatewayResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    public static <T> GatewayResponse<T> fail(String message) {
        return error(500, message);
    }

    public static <T> GatewayResponse<T> fail(int code, String message) {
        return error(code, message);
    }

    public static <T> GatewayResponse<T> error(int code, String message) {
        GatewayResponse<T> response = new GatewayResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    public String toJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"code\":500,\"message\":\"serialization error\"}";
        }
    }
}
