package com.shopper.shopperapi.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private ObjectId id;
    private String name;
    private String details;
    private double cost;
    private String format;
    @Indexed(unique = true)
    private String barCode;
    // private Category categoryId;

/*    public Product(ObjectId id, String name, String details, double cost, String format, String barCode) {
//                   Category categoryId)
        this.id = id;
        this.name = name;
        this.details = details;
        this.cost = cost;
        this.format = format;
        this.barCode = barCode;
        // this.categoryId = categoryId;
    }*/

    @Override
    public String toString() {
        return String.format(
                "Product[name='%s']", name);
    }

    public String getId() {
        return id.toHexString();
    }
}
