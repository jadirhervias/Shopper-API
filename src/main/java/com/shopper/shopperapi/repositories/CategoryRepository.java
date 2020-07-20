package com.shopper.shopperapi.repositories;

import com.shopper.shopperapi.models.Category;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findById(ObjectId id);

    Page<Category> findCategoryPagesById(ObjectId id, Pageable pageable);
}
