package com.shopper.shopperapi.utils.notification;

public enum OrderState {
    PENDING_ORDER_STATE(0),
    ORDER_TAKEN_STATE(1),
    ORDER_ARRIVED_STATE(2),
    ORDER_COMPLETED_STATE(3),
    ORDER_CANCELLED_STATE(4),
    ORDER_IGNORED_STATE(5);

    private final int state;

    OrderState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public static boolean isOrderPendingState(int state) {
        return PENDING_ORDER_STATE.getState() == state;
    }

    public static boolean isOrderTakenState(int state) {
        return ORDER_TAKEN_STATE.getState() == state;
    }

    public static boolean isOrderIgnoredState(int state) {
        return ORDER_IGNORED_STATE.getState() == state;
    }

    public static boolean isOrderArrivedState(int state) {
        return ORDER_ARRIVED_STATE.getState() == state;
    }

    public static boolean isOrderCompletedState(int state) {
        return ORDER_COMPLETED_STATE.getState() == state;
    }

    public static boolean isOrderCancelledState(int state) {
        return ORDER_CANCELLED_STATE.getState() == state;
    }
}
