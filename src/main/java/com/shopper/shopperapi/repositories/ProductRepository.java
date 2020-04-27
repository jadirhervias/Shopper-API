package com.shopper.shopperapi.repositories;

import com.shopper.shopperapi.models.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin(value = {})
public interface ProductRepository extends MongoRepository<Product, String> {
    Product findById(ObjectId id);
}
