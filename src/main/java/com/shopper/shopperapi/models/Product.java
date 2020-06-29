package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "products")
@IgnoreExtraProperties
public class Product {
    @Id
    private ObjectId id = ObjectId.get();
    private String name;
    private String details;
    private double cost;
    private String format;
    private String brand;
    private int stock;
    @DBRef
    private Image image;
    @NotNull
    @Field("last_update")
    @JsonProperty("last_update")
    private String lastUpdate;

	public Product() {
	}

	public String getId() {
        return id.toHexString();
    }
}
