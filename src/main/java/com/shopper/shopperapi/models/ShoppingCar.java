package com.shopper.shopperapi.models;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShoppingCar {

	@NotNull
	@DBRef(db = "products")
	private ObjectId producto;

	@NotNull
	@JsonProperty("quantity")
	private Integer cantidad;

	public ObjectId getProducto() {
		return producto;
	}

	public void setProducto(ObjectId producto) {
		this.producto = producto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

}
