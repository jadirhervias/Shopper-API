package com.shopper.shopperapi.repository;

import com.shopper.shopperapi.model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findById(ObjectId id);
}
