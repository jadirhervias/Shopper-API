package com.shopper.shopperapi.services;

import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.shopper.shopperapi.models.Order;
import com.shopper.shopperapi.models.Pago;
import com.shopper.shopperapi.repositories.OrderRepository;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private final String django = "http://54.200.195.251/api/pagos/";
	
	public List<Order> finAll(){
		return this.orderRepository.findAll();
	}
	
	public String cardOperation(Pago data) throws JSONException{
		
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
}
