package com.shopper.shopperapi.repositories;

import com.shopper.shopperapi.models.SubCategory;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubCategoriyRepository extends MongoRepository<SubCategory, String> {
    SubCategory findById(ObjectId id);

    Page<SubCategory> findSubCategoryPagesById(ObjectId id, Pageable pageable);
}
