package com.shopper.shopperapi.repositories;

import com.shopper.shopperapi.models.Product;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Product findById(ObjectId id);
    
    @Query(value = "{'name': {$regex : ?0, $options: 'i'}}")
    List<Product> findByNameRegex(String producto);
//    Page<Product> findProductPages(List<Product> products, Pageable pageable);
//    List<Product> getCategoryProducts(ObjectId idCategory);
}
