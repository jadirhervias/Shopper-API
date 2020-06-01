package com.shopper.shopperapi.repositories;

import com.shopper.shopperapi.models.Shop;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
//import org.springframework.web.bind.annotation.CrossOrigin;

// No need implementation, just one interface, and you ave CRUD
@Repository
//@CrossOrigin
public interface ShopRepository extends MongoRepository<Shop, String> {
    Shop findById(ObjectId id);

//    @Query(fields = "{_id:0,name:0,last_update:0}")
//    List<Catalog> getCategories(ObjectId id);

//    @Query(fields = "{id:0,name:0,last_update:0}")
//    List<Catalog> getProducts(ObjectId id);
}
