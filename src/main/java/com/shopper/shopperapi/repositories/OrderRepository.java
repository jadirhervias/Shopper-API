package com.shopper.shopperapi.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopper.shopperapi.models.Order;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>{
	Order findById(ObjectId id);
	@Query(value = "{customer.$id.$oid:?0}")
	List<Order> findByCustomerId(String customerId);
}
