package com.shopper.shopperapi.model;

import java.util.List;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

// Para construir un POJO (getters y setters)
// @Document annotation is used for the same purpose with @Entity annotation in JPA
@Data
@Document(collection = "catalogs")
public class Catalog {
    @Id
    private ObjectId id;
    @NotNull
    private String lastUpdate;
    private List<Product> products;

/*
    @DBRef
    private List<UserAddress> addresses;

    public User(){
        addresses = new ArrayList<UserAddress>();
    }
*/

    // Another constructor to populate the entities when creating a new instance.
    // public Catalog() {}

/*    public Catalog(ObjectId id, String lastUpdate, List<Product> products) {
        this.id = id;
        this.lastUpdate = lastUpdate;
        this.products = products;
    }*/

    @Override
    public String toString() {
        return String.format(
            "Catalog[id=%s, lastUpdate='%s']",
            id, lastUpdate
        );
    }

    public String getId() {
        return id.toHexString();
    }
}
