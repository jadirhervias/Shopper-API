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

@Data
@Document(collection = "shops")
public class Shop {

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
    private double shopLat;

    @NotNull
    @Field("shop_lng")
    @JsonProperty("shop_lng")
    private double shopLng;

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

	public double getShopLat() {
		return shopLat;
	}

	public void setShopLat(double shopLat) {
		this.shopLat = shopLat;
	}

	public double getShopLng() {
		return shopLng;
	}

	public void setShopLng(double shopLng) {
		this.shopLng = shopLng;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
    
    
}
