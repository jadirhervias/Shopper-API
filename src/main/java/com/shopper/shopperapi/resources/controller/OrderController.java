package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.Category;
import com.shopper.shopperapi.services.FCMService;
import com.shopper.shopperapi.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@RestController
@RequestMapping(value="/orders", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.COLLECTION_JSON_VALUE })
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private FCMService fcmService;

//    @GetMapping("/arrived/{shopperId}/{customerId}")
    @GetMapping("/arrived")
    public void sendNotificationToCustomer(
//            @PathVariable("shopperId") String shopperId,
//            @PathVariable("customerId") String customerId
    ) {
        fcmService.sendPushNotificationToCustomer(
                "dSA-UxRCapD6SgjezmtKyV:APA91bEbslyJq4OEayObPNmmU4MnKFeXKpcDqMKNzHGXANChL046xizAvJ3tldXhapkIvRQHttkhhSrGBl3IiEyJZDqDUeb3YyPWHpy674ACAUTINNyqqp7UeovOWxvzCb7UOf0R8bE1",
                "Hola, Jadir",
                "Tu pedido ha llegado");
    }
}