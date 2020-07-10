package com.shopper.shopperapi.models;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@Data
public class DeviceGroup {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("registration_ids")
    private List<String> registrationIds;

    public DeviceGroup() {
    }

    public DeviceGroup(String userId, List<String> registrationIds) {
        this.userId = userId;
        this.registrationIds = registrationIds;
    }

    @Override
    public String toString() {
        return "DeviceGroup{" +
                "userId='" + userId + '\'' +
                ", registrationIds=" + registrationIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceGroup that = (DeviceGroup) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(registrationIds, that.registrationIds);
    }
}
