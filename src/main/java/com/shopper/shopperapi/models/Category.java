package com.shopper.shopperapi.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "categories")
public class Category {
    @Id
    private ObjectId id;
    private String name;
    @Field("sub_categories")
    @JsonProperty("sub_categories")
    @DBRef
    private List<SubCategory> subCategories;
//    private List<Product> products;

    @Override
    public String toString() {
        return String.format("Category[name='%s']", name);
    }

    public String getId() {
        return id.toHexString();
    }
}
