package com.shopper.shopperapi.repository;

import com.shopper.shopperapi.model.Catalog;
import com.shopper.shopperapi.model.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findById(ObjectId id);
}
