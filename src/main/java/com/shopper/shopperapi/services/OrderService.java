package com.shopper.shopperapi.services;

import java.util.List;

import com.shopper.shopperapi.models.Order;
import com.shopper.shopperapi.repositories.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	public List<Order> finAll(){
		return this.orderRepository.findAll();
	}
}
