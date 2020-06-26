package com.shopper.shopperapi.utils.notification;

public enum NotificationMessages {
    MESSAGE_TITLE("Hola, "),
    ORDER_ARRIVED_MESSAGE_BODY("Tu pedido ha llegado"),
    ORDER_TAKEN_MESSAGE_BODY("Un shopper ha tomado tu pedido. Te notificaremos cuando llegue");

    private final String message;

    NotificationMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
