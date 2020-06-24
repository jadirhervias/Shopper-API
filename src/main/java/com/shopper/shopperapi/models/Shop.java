package com.shopper.shopperapi.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

// Para construir un POJO (getters y setters)
// @Document annotation is used for the same purpose with @Entity annotation in JPA
@Data
@Document(collection = "shops")
public class Shop {
//    @Id
    @MongoId(FieldType.OBJECT_ID)
    private ObjectId id;
    private String name;
    @NotNull
    @Field("last_update")
    @JsonProperty("last_update")
    @DateTimeFormat
    @LastModifiedDate
    private Date lastUpdate;

    @DBRef
    private List<Category> categories;

    @NotNull
    @Field("shop_lat")
    @JsonProperty("shop_lat")
    private double shop_lat;

    @NotNull
    @Field("shop_lng")
    @JsonProperty("shop_lng")
    private double shop_lng;

    public String getId() {
        return id.toHexString();
    }

    @Override
    public String toString() {
        return String.format(
            "Shop[id=%s, lastUpdate='%s']",
            id, lastUpdate
        );
    }

    public double getShop_lat() {
        return shop_lat;
    }

    public void setShop_lat(double shop_lat) {
        this.shop_lat = shop_lat;
    }

    public double getShop_lng() {
        return shop_lng;
    }

    public void setShop_lng(double shop_lng) {
        this.shop_lng = shop_lng;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
}
