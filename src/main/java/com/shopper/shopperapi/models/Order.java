package com.shopper.shopperapi.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;

import java.util.Objects;

@Data
@Document(collection = "orders")
@IgnoreExtraProperties
public class Order {

//	@Nullable
	@MongoId(FieldType.OBJECT_ID)
	private String id;

	@NotNull
//	@Transient
	@DBRef
	private User customer;

	@Nullable
//	@Transient
	@DBRef
	private User shopper;

	@NotNull
	@Field("shop_id")
	@JsonProperty("shop_id")
	@JsonAlias({"shop_id", "shopId"})
	private String shopId;

	@Transient
	@Nullable
	@JsonProperty("firebase_db_reference_key")
	@JsonAlias({"firebase_db_reference_key", "firebaseDbReferenceKey"})
	private String firebaseDbReferenceKey;

	/**
	 * TODO: CHECK IF IS OK
	 */
	private Coordenates coordenates;

	@org.springframework.lang.Nullable
	@JsonProperty("shopping_car")
	@JsonAlias({"shopping_car", "shoppingCar"})
	private ShoppingCar shoppingCar;
	
	@NotNull
	private int state = 0;
	
//	@NotNull
	@Nullable
	@Field("fecha_compra")
	@JsonProperty("fecha_compra")
	@JsonAlias({"fecha_compra", "fechaCompra"})
	private String fechaCompra;

	@Nullable
	@Field("fecha_entrega")
	@JsonProperty("fecha_entrega")
	@JsonAlias({"fecha_entrega", "fechaEntrega"})
	private String fechaEntrega;
	
	@Nullable
	private String description;

	@Nullable
	private Charge charge;

	@Field("total_cost")
	@JsonProperty("total_cost")
	@JsonAlias({"total_cost", "totalCost"})
	private int totalCost;

	@Field("source_id")
	@JsonProperty("source_id")
	@JsonAlias({"source_id", "sourceId"})
	private String sourceId;

	@Field("operation_id")
	@JsonProperty("operation_id")
	private String operationId;

	public Order(){
	}

	// CUSTOM GETTERS
	@Nullable
	@Exclude
	public Charge getCharge() {
		return charge;
	}

	@Override
	public String toString() {
		return "Order{" +
				"id='" + id + '\'' +
				", customer=" + customer +
				", shopper=" + shopper +
				", shopId='" + shopId + '\'' +
				", firebaseDbReferenceKey='" + firebaseDbReferenceKey + '\'' +
				", coordenates=" + coordenates +
				", shoppingCar=" + shoppingCar +
				", state=" + state +
				", fechaCompra='" + fechaCompra + '\'' +
				", fechaEntrega='" + fechaEntrega + '\'' +
				", description='" + description + '\'' +
				", charge=" + charge +
				", totalCost=" + totalCost +
				", sourceId='" + sourceId + '\'' +
				", operationId='" + operationId + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return state == order.state &&
				totalCost == order.totalCost &&
				Objects.equals(id, order.id) &&
				Objects.equals(customer, order.customer) &&
				Objects.equals(shopper, order.shopper) &&
				Objects.equals(shopId, order.shopId) &&
				Objects.equals(firebaseDbReferenceKey, order.firebaseDbReferenceKey) &&
				Objects.equals(coordenates, order.coordenates) &&
				Objects.equals(shoppingCar, order.shoppingCar) &&
				Objects.equals(fechaCompra, order.fechaCompra) &&
				Objects.equals(fechaEntrega, order.fechaEntrega) &&
				Objects.equals(description, order.description) &&
				Objects.equals(charge, order.charge) &&
				Objects.equals(sourceId, order.sourceId) &&
				Objects.equals(operationId, order.operationId);
	}
}
