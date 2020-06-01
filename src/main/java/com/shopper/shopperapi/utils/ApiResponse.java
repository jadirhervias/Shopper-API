package com.shopper.shopperapi.utils;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApiResponse {

    private  int status;
    private  boolean success;
    private  String message;
    private  String timestamp;
    private  Object result;

    public ApiResponse(int status, String message, boolean success) {
        this.status = status;
        this.message = message;
        this.success = success;
        this.timestamp = Instant.now().toString();
    }

}