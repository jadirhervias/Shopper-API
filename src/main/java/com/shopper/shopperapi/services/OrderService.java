package com.shopper.shopperapi.services;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.*;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.shopper.shopperapi.models.Order;
import com.shopper.shopperapi.models.Charge;
import com.shopper.shopperapi.repositories.OrderRepository;

import com.shopper.shopperapi.utils.FirebaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shopper.shopperapi.utils.notification.NotificationMessages.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private FCMService fcmService;
	@Autowired
	private FirebaseConfiguration firebaseConfiguration;

	private final String django = "http://54.200.195.251/api/pagos/";

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * TODO: Funcionalidad para ubicar al shopper(S) más cercano al customer
	 */

	// History of orders
	public List<Order> finAll(){
		return this.orderRepository.findAll();
	}

	// Process order charge
	public boolean processOrder(Order order, Charge charge, String customerNotificationKey) throws JSONException {
		boolean cardVerified = cardOperation(charge);

		if (cardVerified) {

			// Obtener lista de notification keys de shopper más cercano(s)
			List<String> shoppersDeviceGroupKey = new ArrayList<>();
			shoppersDeviceGroupKey.add("APA91bHhj2mg_MVQHPouK7WwkUb9xlhd52q4kQS9-OGKKBC1eo3L1A3UWb1MbH-ELXKf8Z3nslRfC-l-cGHp6bxEynD8k19axMEmL5c0BVK_elF-l3jyAzvvEl6-BCYQjOfBG-t4LXqDzgO_vED6t3qQCvuC9fbwvw");

			// Call to firebase database - publicar la orden y notificar a shoppera más cercanos
			newOrder(customerNotificationKey, shoppersDeviceGroupKey, order);

			// Petición para que el shopper acepte o cancele -> obtener codigo de estado 1 2 3


			// Actualizar el estado de la orden a exitosa 2 o cancelada 3



		} else {
			System.out.println("card problem");
		}

		return true;
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

	// Expose order in Firebase cloud
	public void newOrder(String customerNotificationKey, List<String> shoppersDeviceGroupKey, Order order) {

		DatabaseReference ordersRef = firebaseConfiguration.firebaseDatabase().child("orders");

		// Generate a reference to a new location and add some data using push()
		DatabaseReference pushedOrderRef = ordersRef.push();

		// Get the unique ID generated by a push()
		String orderId = pushedOrderRef.getKey();

		ordersRef.child(orderId).setValue("New Order", (databaseError, databaseReference) -> {
			if (databaseError != null) {
				System.out.println("Data could not be saved " + databaseError.getMessage());
			} else {

				// Notificar al shopper(s) más cercano
				System.out.println("Order data saved successfully.");

				fcmService.sendPushNotificationToShoppers(customerNotificationKey, shoppersDeviceGroupKey,
						MESSAGE_TITLE.getMessage(), NEW_ORDER_MESSAGE_BODY.getMessage());
			}
		});

//		ordersRef.child(orderId).setValueAsync(order);
	}

	public void updateOrderState(String OrderKey) {

	}

	public boolean cardOperation(Charge data) throws JSONException {

		boolean success;
		
		JSONObject param = new JSONObject();
		param.put("amount", data.getAmount());
		param.put("currency_code", data.getCurrency_code());
		param.put("description", data.getDescription());
		param.put("email", data.getEmail());
		param.put("source_id", data.getSource_id());
	
		HttpEntity<String> httpEntity = new HttpEntity<>(param.toString());
		
		ResponseEntity<String> 	msm = restTemplate.exchange(django, HttpMethod.POST, httpEntity, String.class);

		if (msm.getBody().equals("201")) {
			success = true;
		} else {
			success = false;
		}

		return success;
	}
}
