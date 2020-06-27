package com.shopper.shopperapi.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.geo.Distance;

import com.shopper.shopperapi.models.Order;
import com.shopper.shopperapi.models.OrderShopper;
import com.shopper.shopperapi.models.Shop;
import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.models.Charge;
import com.shopper.shopperapi.repositories.OrderRepository;
import com.shopper.shopperapi.repositories.ShopRepository;
import com.shopper.shopperapi.repositories.UserRepository;
import com.shopper.shopperapi.utils.distance.DistanceCalculated;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private final String django = "http://54.200.195.251/api/pagos/";
	
	public List<Order> finAll(){
		return this.orderRepository.findAll();
	}
	
	private String cardOperation(Charge data) throws JSONException{
		
		String estado = null;
		
		JSONObject param=new JSONObject();
		param.put("amount", data.getAmount());
		param.put("currency_code", data.getCurrency_code());
		param.put("description", data.getDescription());
		param.put("email", data.getEmail());
		param.put("source_id", data.getSource_id());
	
		HttpEntity<String> httpEntity = new HttpEntity<String>(param.toString());
		
		ResponseEntity<String> 	msm = restTemplate.exchange(django, HttpMethod.POST, httpEntity, String.class);
		
		if (msm.getBody().equals("201")) {
			estado = "201";
		}else {
			estado = "500";
		}
		return estado;
	}
	
	public List<String> shoperList(String id_shop){
		List<User> usuario = userRepository.findByRole("ROLE_SHOPPER");
		List<OrderShopper> orderShopper = new ArrayList<OrderShopper>();
		List<String> notification_key = new ArrayList<>();
		Shop shop = shopRepository.findById(id_shop).get();
		for(int i=0;i<usuario.size();i++) {
			Double distancia = DistanceCalculated.distanceCoord(usuario.get(i).getUserLat(), usuario.get(i).getUserLng(), shop.getShopLat(), shop.getShopLng());
			if(distancia<=100) {
				orderShopper.add(new OrderShopper(distancia,usuario.get(i).getNotificationDeviceGroup().get("notification_key")));
			
			}
		}
		orderShopper.sort(Comparator.comparing(OrderShopper::getDistance));
		for (OrderShopper orderShopper2 : orderShopper) {
			notification_key.add(orderShopper2.getNotification_key());
		}
		return notification_key;
	} 
}
