package com.shopper.shopperapi.utils.notification;

public enum DeviceGroupsOperations {
    CREATE_DEVICE_GROUP("create"),
    ADD_NOTIFICATION_KEY("add"),
    REMOVE_NOTIFICATION_KEY("remove");

    private final String operation;

    DeviceGroupsOperations(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
