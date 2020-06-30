package com.shopper.shopperapi.models;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class OrderShopper {
    @NotNull
    private double distance;

    @NotNull
    /**
     * TODO: CHANGE THIS
     */
    private String notification_key;

    public OrderShopper(@NotNull double distance, @NotNull String notification_key) {
        super();
        this.distance = distance;
        this.notification_key = notification_key;
    }
}
