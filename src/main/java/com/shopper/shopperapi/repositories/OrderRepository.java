package com.shopper.shopperapi.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shopper.shopperapi.models.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>{
	Order findById(ObjectId id);
}
