package com.shopper.shopperapi.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.google.firebase.database.*;
import com.shopper.shopperapi.models.*;
import net.minidev.json.JSONObject;
import com.shopper.shopperapi.utils.distance.DistanceCalculated;
import org.bson.types.ObjectId;

import com.shopper.shopperapi.repositories.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shopper.shopperapi.utils.notification.OrderState.PENDING_ORDER_STATE;
import static com.shopper.shopperapi.utils.notification.OrderState.isOrderTakenState;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nullable;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private DatabaseReference databaseReference;
	@Autowired
	private RestTemplate restTemplate;
	private final String DJANGO_API = "http://54.218.120.209/api/pagos/";
	private final String INFO_OPERATION = "https://api.culqi.com/v2/charges/";

	/**
	 * TODO: Funcionalidad para ubicar al shopper(S) más cercano al customer
	 */

	// All completed/cancelled orders
	public List<Order> findAll(){
		return this.orderRepository.findAll();
	}

	public Order findOrder(String orderId) {
		Order order = this.orderRepository.findById(orderId).get();
		order.setCustomer(null);
		if (order.getShopper() != null) {
			order.getShopper().setPassword(null);
			order.getShopper().setRole(null);
			order.getShopper().setAddress(null);
			order.getShopper().setShoppingCars(null);
			order.getShopper().setPhoneNumber(null);
			order.getShopper().setUserLat(0);
			order.getShopper().setUserLng(0);
			order.getShopper().setNotificationDeviceGroup(null);
		}
		return order;
	}

	// Completed/cancelled orders by customer id
	public List<Order> findOrdersByCustomerId(String customerId) throws ParseException {
		List<Order> orders = this.orderRepository.findAll();

		// To sort by date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,1);
		String firstDayMonth = sdf.format(calendar.getTime());
		calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+1);
		String nextFirstDayMonth = sdf.format(calendar.getTime());
		//Tranformandolo a date
		Date nextFirstDayMonthDate = sdf.parse(nextFirstDayMonth);
		Date firstDayMonthDate = sdf.parse(firstDayMonth);
		// To sort by date

		return orders.stream()
				.map((order) -> {
					try {
						Date orderDate = sdf.parse(order.getFechaCompra());
						if (orderDate.after(firstDayMonthDate) && orderDate.before(nextFirstDayMonthDate)) {
							if (order.getCustomer().getId().equals(customerId)) {
								order.getCustomer().setShoppingCars(null);
								order.getCustomer().setNotificationDeviceGroup(null);
								order.getCustomer().setPassword(null);
								if (order.getShopper() != null) {
									order.getShopper().setShoppingCars(null);
									order.getShopper().setNotificationDeviceGroup(null);
									order.getShopper().setPassword(null);
								}
								return order;
							}
							return null;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return null;
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Obtener una lista de ordenes paginadas por id de customer
	 * @param customerId-
	 * @param pageable-
	 * @return Page<Order>
	 */
	public Page<Order> findOrderPageByCustomerId(String customerId, Pageable pageable) throws ParseException {

		List<Order> userOrders = this.findOrdersByCustomerId(customerId);

		int start = (int) pageable.getOffset();

		int end = Math.min((start + pageable.getPageSize()), userOrders.size());

		Page<Order> ordersPage = new PageImpl<>(userOrders.subList(start, end), pageable, userOrders.size());

		return ordersPage;
	}

	// Pending orders by customer id
//	public Order findPendingOrderByCustomerId(){
//		DatabaseReference ordersRef = databaseReference.child("orders");
//		ordersRef.
//	}

	/**
	 * Método para crear una orden
	 * @param order objeto Order
	 * @return Order
	 */
	@Transactional
	public Order create(Order order) {
//        order.setId(ObjectId.get().toHexString());
		return this.orderRepository.save(order);
	}

	public String getOrderFirebaseDbRefKey(String orderId) {
		return orderRepository.findById(orderId).get().getFirebaseDbReferenceKey();
	}

	// Process order charge
	public boolean processOrder(Order order) {
		User customer = order.getCustomer();

		System.out.println(order.getSourceId());
		System.out.println(customer.getId());
		System.out.println(customer.getEmail());
		System.out.println(order.getTotalCost());

		if (order.getTotalCost() >= 300) {
			NewChargeResponse cardVerified = cardOperation(order.getSourceId(), customer.getId(), customer.getEmail(), order.getTotalCost());

			if (cardVerified.getStatus() != null) {
				// Call to firebase database - publicar la orden y notificar a shoppera más cercanos
				newOrder(order, cardVerified.getId());
				return true;
			} else {
				System.out.println("card problem");
				return false;
			}
		}

		System.out.println("Order not valid (not enough amount)");
		return false;
	}

	public boolean handleOrder(String orderFirebaseDbRefKey, int state, String shopperId) {
		// Petición para que el shopper acepte o cancele -> obtener codigo de estado 1 2 3
		try {
			updateOrderByState(orderFirebaseDbRefKey, state, shopperId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Expose order in Firebase cloud
	public void newOrder(Order order, String idOperation) {

		DatabaseReference ordersRef = databaseReference.child("orders");

		// Generate a reference to a new location and add some data using push()
		DatabaseReference pushedOrderRef = ordersRef.push();

		// Get the unique ID generated by a push()
		String orderFirebaseDbRefKey = pushedOrderRef.getKey();
		order.setFirebaseDbReferenceKey(orderFirebaseDbRefKey);
		order.setId(ObjectId.get().toHexString());
		order.setState(PENDING_ORDER_STATE.getState());
		order.setOperationId(idOperation);

		// Hide sensitive data
		if (order.getShopper() != null) {
			order.getShopper().setPassword(null);
		}

		order.getCustomer().setPassword(null);

		ordersRef.child(orderFirebaseDbRefKey).setValue(order, (databaseError, databaseReference) -> {
			if (databaseError != null) {
				System.out.println("Data could not be saved " + databaseError.getMessage());
			} else {
				// Notificar al shopper(s) más cercano
				System.out.println("Order data saved successfully.");

				// ...
			}
		});
	}

	public void deleteFirebaseOrder(Order order) {

		System.out.println("ORDER TO DELETE:");
		System.out.println(order);

		try {
			Order orderAdded = orderRepository.save(order);
			System.out.println("Order in Mongo:");
			System.out.println(orderAdded);
		} catch (Exception e) {
			System.out.println("Exception");
			System.out.println(e);
		}

		DatabaseReference ordersRef = databaseReference.child("orders");

		String dbKey = order.getFirebaseDbReferenceKey();

		System.out.println("LLAVE A REMOVER DE FIREBASE: " + dbKey);

		ordersRef.child(dbKey).removeValue((databaseError, databaseReference) -> {
			if (databaseError != null) {
				System.out.println("Data could not be saved " + databaseError.getMessage());
			} else {
				System.out.println("Order data saved successfully.");
			}
		});
	}

	public void updateOrderByState(String orderFirebaseDbRefKey, int state, @Nullable String idShopper) {

		DatabaseReference ordersRef = databaseReference.child("orders");

		System.out.println("SE ACTUALIZARA EL ESTADO DE LA ORDEN : " + orderFirebaseDbRefKey);
		System.out.println("QUE SHOPPER TOMÓ LA ORDEN: " + idShopper);

		Map<String, Object> newOrderState = new HashMap<>();

		if (isOrderTakenState(state) && idShopper != null) {
			newOrderState.put("shopper", userService.findById(new ObjectId(idShopper)).get());
		}

		newOrderState.put("state", state);

		ordersRef.child(orderFirebaseDbRefKey).updateChildren(newOrderState, (databaseError, databaseReference) -> {
			if (databaseError != null) {
				System.out.println("Order could not be updated " + databaseError.getMessage());
			} else {
				// Notificar al shopper(s) más cercano
				System.out.println("Order data updated successfully.");

				// ...

			}
		});
	}

	public NewChargeResponse cardOperation(String sourceId, String customerId, String customerEmail, int totalCost) {

		NewChargeResponse response = new NewChargeResponse();

		JSONObject param = new JSONObject();
		param.put("amount", totalCost);
		param.put("currency_code", "PEN");
		param.put("description", "appOrder-" + customerId);
		param.put("email", customerEmail);
		param.put("source_id", sourceId);

		HttpEntity<String> httpEntity = new HttpEntity<>(param.toString());

		ResponseEntity<NewChargeResponse> charge = restTemplate.exchange(DJANGO_API, HttpMethod.POST, httpEntity, NewChargeResponse.class);

		if (charge.getBody() != null && charge.getBody().getStatus().equals("201")) {
			response.setId(charge.getBody().getId());
			response.setStatus(charge.getBody().getStatus());
		} else {
			response.setId(null);
			response.setStatus(null);
		}

		return response;
	}

	public InfoOperationCard operation(String id_operation) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer sk_test_ifqClViyXgOFU5R6");
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		ResponseEntity<CardResponse> info = restTemplate.exchange(INFO_OPERATION, HttpMethod.GET, httpEntity, CardResponse.class);
		return Objects.requireNonNull(info.getBody()).getInformation().get(0);
	}

	/**
	 * Método para obtener lista de SHOPPERS más cercanos al pedido en orden de distancia
	 * @param idShop-
	 * @return List<String>
	 */
	public List<String> shoperList(String idShop) {

		List<User> usuario = userService.findByRole("ROLE_SHOPPER");
		List<OrderShopper> orderShopper = new ArrayList<>();
		List<String> notification_key = new ArrayList<>();
		Shop shop = shopService.findById(new ObjectId(idShop));

		for (User user : usuario) {
			Double distancia = DistanceCalculated.distanceCoord(user.getUserLat(), user.getUserLng(), shop.getShopLat(), shop.getShopLng());
			if (distancia <= 100) {
				orderShopper.add(new OrderShopper(distancia, user.getNotificationDeviceGroup().get("notification_key")));
			}
		}

		orderShopper.sort(Comparator.comparing(OrderShopper::getDistance));

		for (OrderShopper orderShopper2 : orderShopper) {
			notification_key.add(orderShopper2.getNotification_key());
		}

		return notification_key;
	}
}
