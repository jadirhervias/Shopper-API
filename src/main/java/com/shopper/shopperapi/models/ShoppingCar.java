package com.shopper.shopperapi.models;

import javax.validation.constraints.NotNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "shopping_cars")
@IgnoreExtraProperties
public class ShoppingCar {

	@Id
	private ObjectId id = ObjectId.get();

	@NotNull
	@DBRef
	private List<Product> products;

	@NotNull
	private int count;

	public ShoppingCar() {
	}

	public String getId() {
		return id.toHexString();
	}

	//	public ObjectId getObjectId() {
//		return id;
//	}

}
