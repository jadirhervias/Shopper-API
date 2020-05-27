package com.shopper.shopperapi.repositories;

import com.shopper.shopperapi.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
//    User findById(ObjectId id);
    Optional<User> findById(ObjectId id);
    @Query(value = "{email:?0}")
    Optional<User> findByEmail(String email);
//    User findByEmail(String email);
}
