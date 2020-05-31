package com.shopper.shopperapi.models;

import java.util.List;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "categories")
public class Category {
    @Id
    private ObjectId id = ObjectId.get();
    private String name;
    @DBRef
    private List<Product> products;

    @Override
    public String toString() {
        return String.format("Category[name='%s']", name);
    }

    public String getId() {
        return id.toHexString();
    }
}
