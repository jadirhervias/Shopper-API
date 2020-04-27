package com.shopper.shopperapi.repositories;

import com.shopper.shopperapi.models.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin(value = {})
public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findById(ObjectId id);
}
