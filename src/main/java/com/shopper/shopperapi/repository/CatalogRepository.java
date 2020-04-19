package com.shopper.shopperapi.repository;

import java.util.List;

import com.shopper.shopperapi.model.Catalog;
import com.shopper.shopperapi.model.Category;
import com.shopper.shopperapi.model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

// No need implementation, just one interface, and you have CRUD
@Repository
public interface CatalogRepository extends MongoRepository<Catalog, String> {
    Catalog findById(ObjectId id);
//    @Query(fields = "{ 'id' : 0, 'lastUpdate' : 0}")
//    List<Product> listCatalogProductsById(ObjectId id);
//    List<Category> listCategories(ObjectId id);
//    @Query(fields = "{ 'id' : 0, 'lastUpdate' : 0}")
//    List<Product> getCategoryProductsById(ObjectId idCatalog, ObjectId idCategory);
}
