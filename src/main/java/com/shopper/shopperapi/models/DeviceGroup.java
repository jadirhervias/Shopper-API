package com.shopper.shopperapi.models;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Data
public class DeviceGroup {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("registration_ids")
    private List<String> registrationIds;
}
