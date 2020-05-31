package com.shopper.shopperapi.repositories;

import com.shopper.shopperapi.models.Catalog;
import com.shopper.shopperapi.models.Category;
import com.shopper.shopperapi.models.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

// No need implementation, just one interface, and you have CRUD
@Repository
public interface CatalogRepository extends MongoRepository<Catalog, String> {
    Catalog findById(ObjectId id);
//    Catalog findById(String id);

//    @Query(fields = "{_id:0,name:0,last_update:0}")
//    List<Catalog> getCategories(ObjectId id);

//    @Query(fields = "{id:0,name:0,last_update:0}")
//    List<Catalog> getProducts(ObjectId id);
}
