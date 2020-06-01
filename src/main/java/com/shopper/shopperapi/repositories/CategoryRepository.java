package com.shopper.shopperapi.repositories;

import com.shopper.shopperapi.models.Category;
import com.shopper.shopperapi.models.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findById(ObjectId id);

//    Page<Product> findProductPagesById(ObjectId id, Pageable pageable);
    Page<Category> findCategoryPagesById(ObjectId id, Pageable pageable);

    @Query(value = "{_id:?0}", fields="{_id : 0, name : 0, }")
    List<Product> findProductsById(ObjectId id);
}
