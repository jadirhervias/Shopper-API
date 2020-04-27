package com.shopper.shopperapi.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

// Para construir un POJO (getters y setters)
// @Document annotation is used for the same purpose with @Entity annotation in JPA
@Data
@Document(collection = "catalogs")
public class Catalog {
    @Id
    private ObjectId id;
    private String name;
    @NotNull
    @Field("last_update")
    @JsonProperty("last_update")
    private String lastUpdate;
    @DBRef
    private List<Category> categories;

/*
    @DBRef
    private List<UserAddress> addresses;

    public User(){
        addresses = new ArrayList<UserAddress>();
    }
*/

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
