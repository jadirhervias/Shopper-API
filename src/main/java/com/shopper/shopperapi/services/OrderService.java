package com.shopper.shopperapi.services;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.*;
import com.shopper.shopperapi.models.Order;
import com.shopper.shopperapi.repositories.OrderRepository;

import com.shopper.shopperapi.utils.FirebaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shopper.shopperapi.utils.notification.NotificationMessages.*;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private FCMService fcmService;
	@Autowired
	private FirebaseConfiguration firebaseConfiguration;

	/**
	 * TODO: Funcionalidad para ubicar al shopper(S) más cercano al customer
	 */

	// History of orders
	public List<Order> finAll(){
		return this.orderRepository.findAll();
	}

	// Process order charge
	public boolean processPay(){
		return true;
	}

	// Expose order in the cloud
	public void newOrder(String customerNotificationKey
//						 , Order order
	) {

		DatabaseReference ordersRef = firebaseConfiguration.firebaseDatabase().child("orders");

		// Generate a reference to a new location and add some data using push()
		DatabaseReference pushedOrderRef = ordersRef.push();

		// Get the unique ID generated by a push()
		String orderId = pushedOrderRef.getKey();

		ordersRef.child(orderId).setValue("New Order", (databaseError, databaseReference) -> {
			if (databaseError != null) {
				System.out.println("Data could not be saved " + databaseError.getMessage());
			} else {
				System.out.println("Order data saved successfully.");

				boolean payIsValid = this.processPay();

				// Notificar al shopper(s) más cercano
				if (payIsValid) {
					// lista de notification key de usuarios SHOPPER
					List<String> shoppersDeviceGroupKey = new ArrayList<>();
					shoppersDeviceGroupKey.add("APA91bHhj2mg_MVQHPouK7WwkUb9xlhd52q4kQS9-OGKKBC1eo3L1A3UWb1MbH-ELXKf8Z3nslRfC-l-cGHp6bxEynD8k19axMEmL5c0BVK_elF-l3jyAzvvEl6-BCYQjOfBG-t4LXqDzgO_vED6t3qQCvuC9fbwvw");

					fcmService.sendPushNotificationToShoppers(customerNotificationKey, shoppersDeviceGroupKey,
							MESSAGE_TITLE.getMessage(), NEW_ORDER_MESSAGE_BODY.getMessage());
				}
			}
		});

//		ordersRef.child(orderId).setValueAsync(order);
	}

	/**
	 * Método para crear una orden
	 * @param order
	 * @return Order
	 */
	@Transactional
	public Order create(Order order) {
//        shop.setId(ObjectId.get());
		return this.orderRepository.save(order);
	}
}
