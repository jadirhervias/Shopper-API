package com.shopper.shopperapi.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shopper.shopperapi.models.Purchase;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, String>{
	Purchase findByUser(ObjectId id);
}
