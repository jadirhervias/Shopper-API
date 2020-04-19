package com.shopper.shopperapi.model;

import java.util.List;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "categories")
public class Category {
    @Id
    private ObjectId id;
    private String name;
    private List<Product> products;

/*    public Category(ObjectId _id, String name) {
        this._id = _id;
        this.name = name;
    }*/

    @Override
    public String toString() {
        return String.format("Category[name='%s']", name);
    }

    public String getId() {
        return id.toHexString();
    }
}
