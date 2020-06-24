package com.shopper.shopperapi.services;

import java.util.List;

import com.shopper.shopperapi.models.Purchase;
import com.shopper.shopperapi.repositories.PurchaseRepository;

public class PurchaseService {
	private final PurchaseRepository purchaseRepository;
	
	public PurchaseService(PurchaseRepository purchaseRepository) {
		this.purchaseRepository = purchaseRepository;
	}
	
	public List<Purchase> finAll(){
		return this.purchaseRepository.findAll();
	}
}
