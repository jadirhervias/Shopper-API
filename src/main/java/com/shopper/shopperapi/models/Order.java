package com.shopper.shopperapi.models;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;

@Data
@Document(collection = "orders")
public class Order {

	@MongoId(FieldType.OBJECT_ID)
	private ObjectId id = ObjectId.get();

	@NotNull
	@DBRef
	private User customer;

	@Nullable
	@DBRef
	private User shopper;

	@NotNull
	@Field("shop_id")
	@JsonProperty("shop_id")
	private String shopId;

	@Transient
	@Nullable
	@JsonProperty("firebase_db_reference_key")
	private String firebaseDbReferenceKey;

	/**
	 * TODO: CHECK IF IS OK
	 */
//	@Nullable
	private Coordenates coordenates;

	@Nullable
	@DBRef
	@Field("shopping_car")
	@JsonProperty("shopping_car")
	private ShoppingCar shoppingCar;
	
	@NotNull
	private int state = 0;
	
	@NotNull
	@Field("fecha_compra")
	@JsonProperty("fecha_compra")
	private Date fechaCompra;

	@Nullable
	@Field("fecha_entrega")
	@JsonProperty("fecha_entrega")
	private Date fechaEntrega;
	
	@Nullable
	private String description;

	@Field("total_cost")
	@JsonProperty("total_cost")
	private int totalCost;

	@Field("source_id")
	@JsonProperty("source_id")
	private String sourceId;

	public Order(){
	}

	public String getId() {
		return id.toHexString();
	}
}
