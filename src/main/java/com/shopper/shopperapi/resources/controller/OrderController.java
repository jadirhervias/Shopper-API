package com.shopper.shopperapi.resources.controller;

//import com.shopper.shopperapi.models.Charge;
import com.shopper.shopperapi.models.Order;
//import com.shopper.shopperapi.services.FCMService;
import com.shopper.shopperapi.services.OrderService;
//import com.shopper.shopperapi.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shopper.shopperapi.utils.notification.NotificationMessages.*;
import static com.shopper.shopperapi.utils.notification.OrderState.*;


import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/orders", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.COLLECTION_JSON_VALUE })
public class OrderController {

    @Autowired
    private OrderService orderService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private FCMService fcmService;

//    @PostMapping("/{customerId}")
    @PostMapping
    @PreAuthorize("hasAuthority('orders:write')")
    public ResponseEntity<?> createOrder(
//            @PathVariable("customerId") String customerId,
            @RequestBody Order order)
            throws JSONException {

        boolean success = orderService.processOrder(order);

        if (!success) {
            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        }

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/take/{shopperId}/{orderFirebaseDbRefKey}")
    @PreAuthorize("hasRole('ROLE_SHOPPER')")
    public ResponseEntity<?> takeOrder(
            @PathVariable("shopperId") String shopperId,
            @PathVariable("orderFirebaseDbRefKey") String orderFirebaseDbRefKey) {
        orderService.handleOrder(orderFirebaseDbRefKey, ORDER_TAKEN_STATE.getState(), shopperId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * TODO: IGNORE ORDER FEATURE - SHOPPER USER
     */

    @PostMapping("/arrived/{shopperId}/{orderFirebaseDbRefKey}")
    @PreAuthorize("hasRole('ROLE_SHOPPER')")
    public ResponseEntity<?> orderArrived(
            @PathVariable("shopperId") String shopperId,
            @PathVariable("orderFirebaseDbRefKey") String orderFirebaseDbRefKey) {
        orderService.handleOrder(orderFirebaseDbRefKey, ORDER_ARRIVED_STATE.getState(), shopperId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/ok/{shopperId}/{orderFirebaseDbRefKey}")
    @PreAuthorize("hasRole('ROLE_SHOPPER')")
    public ResponseEntity<?> completeOrder(
            @PathVariable("shopperId") String shopperId,
            @PathVariable("orderFirebaseDbRefKey") String orderFirebaseDbRefKey) {
        orderService.handleOrder(orderFirebaseDbRefKey, ORDER_COMPLETED_STATE.getState(), shopperId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/cancel/{shopperId}/{orderFirebaseDbRefKey}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> cancelOrder(
            @PathVariable("shopperId") String shopperId,
            @PathVariable("orderFirebaseDbRefKey") String orderFirebaseDbRefKey) {
        orderService.handleOrder(orderFirebaseDbRefKey, ORDER_CANCELLED_STATE.getState(), shopperId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PostMapping("/arrived/{shopperId}/{customerId}")
//    @PreAuthorize("hasRole('ROLE_SHOPPER')")
//    public void sendNotificationToCustomer(
//            @PathVariable("shopperId") String shopperId,
//            @PathVariable("customerId") String customerId
//    ) {
//
//        String userName = userService.getUserFirstName(customerId);
//        String userDeviceGroupKey = userService.getUserNotificationKey(customerId);
//        String senderKey = userService.getUserNotificationKey(shopperId);
//
//        fcmService.sendPushNotificationToCustomer(
//                userDeviceGroupKey,
//                senderKey,
//                MESSAGE_TITLE.getMessage() + userName,
//                ORDER_ARRIVED_MESSAGE_BODY.getMessage());
//    }
}