package com.shopper.shopperapi.repositories;

import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.models.SubCategory;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoriyRepository extends MongoRepository<SubCategory, String> {
    SubCategory findById(ObjectId id);

    Page<SubCategory> findSubCategoryPagesById(ObjectId id, Pageable pageable);

    @Query(value = "{_id:?0}", fields="{_id : 0, name : 0, }")
    List<Product> findProductsById(ObjectId id);
}
