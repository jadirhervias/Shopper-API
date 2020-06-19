package com.shopper.shopperapi.repositories;

import com.shopper.shopperapi.models.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Product findById(ObjectId id);
//    Page<Product> findProductPages(List<Product> products, Pageable pageable);
//    List<Product> getCategoryProducts(ObjectId idCategory);
}
