package com.shopper.shopperapi.models;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Shopping {

	@NotNull
	@DBRef(db = "products")
	private ObjectId producto;
	
	
	@NotNull
	@JsonProperty("quantity")
	private Integer cantidad;
	
}
