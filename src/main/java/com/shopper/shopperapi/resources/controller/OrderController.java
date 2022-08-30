package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.InfoOperationCard;
import com.shopper.shopperapi.models.Order;
import com.shopper.shopperapi.services.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.shopper.shopperapi.utils.notification.OrderState.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/orders", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.COLLECTION_JSON_VALUE })
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> getOrders(
            @PathVariable("customerId") String customerId) throws ParseException {
        List<?> orders = orderService.findOrdersByCustomerId(customerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{customerId}/pagination")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> getOrdersPage(
            @PathVariable("customerId") String customerId,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy) throws ParseException {
        Page<Order> ordersPage = this.orderService.findOrderPageByCustomerId(
                customerId,
                PageRequest.of(
                        page.orElse(0),
                        5,
                        Sort.Direction.ASC,
                        sortBy.orElse("id")
                )
        );
        return new ResponseEntity<>(ordersPage, HttpStatus.OK);
    }

    @GetMapping("/find/{orderId}")
    public ResponseEntity<?> orderDetail(@PathVariable("orderId")String orderId){
        Order order = orderService.findOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('orders:write')")
    public ResponseEntity<?> newOrder(@RequestBody @Valid Order order) {
        System.out.println(order);
        orderService.create(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('orders:write')")
    public ResponseEntity<?> createOrder(@RequestBody @Valid Order order)
            throws JSONException {
        try {
            boolean success = orderService.processOrder(order);
            if (!success) {
                return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
            }

            logger.info("Order in response" + order);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            System.out.println("EX: " + e);
            e.getCause();
//            System.out.println(e.getCause());
        }
        logger.info("Order in response" + order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PostMapping("/take/{shopperId}/{orderFirebaseDbRefKey}")
    @PreAuthorize("hasRole('ROLE_SHOPPER')")
    public ResponseEntity<?> takeOrder(
            @PathVariable("shopperId") String shopperId,
            @PathVariable("orderFirebaseDbRefKey") String orderFirebaseDbRefKey) {
        /**
         * TODO: HANDLE ERROR USING BOOLEAN RESULT OF THIS FUNCTION:
         */
        orderService.handleOrder(orderFirebaseDbRefKey, ORDER_TAKEN_STATE.getState(), shopperId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * TODO: FEATURE TO IGNORE ORDER FOR THE SHOPPER USER
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

    @GetMapping("/operation/{id}")
    public ResponseEntity<?> infoOperatio(@PathVariable("id")String idOperation){
        InfoOperationCard infoOperationCard = this.orderService.operation(idOperation);
        return new ResponseEntity<>(infoOperationCard,HttpStatus.OK);
    }
}
