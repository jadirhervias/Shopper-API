package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.Charge;
import com.shopper.shopperapi.models.Order;
import com.shopper.shopperapi.services.FCMService;
import com.shopper.shopperapi.services.OrderService;
import com.shopper.shopperapi.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.shopper.shopperapi.utils.notification.NotificationMessages.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/orders", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.COLLECTION_JSON_VALUE })
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private FCMService fcmService;

    @PostMapping("/{customerId}")
    @PreAuthorize("hasAuthority('orders:write')")
    public ResponseEntity<?> createOrder(@PathVariable("customerId") String customerId, @RequestBody @Valid Order order)
            throws JSONException {
        Charge charge = new Charge();

        String customerDeviceGroupKey = userService.getUserNotificationKey(customerId);

//        orderService.newOrder(customerDeviceGroupKey, order);
        orderService.processOrder(order, charge, customerDeviceGroupKey);

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/arrived/{shopperId}/{customerId}")
    @PreAuthorize("hasRole('ROLE_SHOPPER')")
    public void sendNotificationToCustomer(
            @PathVariable("shopperId") String shopperId,
            @PathVariable("customerId") String customerId
    ) {

        String userName = userService.getUserFirstName(customerId);
        String userDeviceGroupKey = userService.getUserNotificationKey(customerId);
        String senderKey = userService.getUserNotificationKey(shopperId);

        fcmService.sendPushNotificationToCustomer(
                userDeviceGroupKey,
                senderKey,
                MESSAGE_TITLE.getMessage() + userName,
                ORDER_ARRIVED_MESSAGE_BODY.getMessage());
    }
}