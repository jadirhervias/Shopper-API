package com.shopper.shopperapi.models;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;

import lombok.Builder;

@Document(collection = "purchases")
public class Order {

	@MongoId(FieldType.OBJECT_ID)
	private ObjectId id;
	
	@NotNull
	@Field("id_user")
	@DBRef(db = "users")
	@JsonProperty("id_user")
	private User userId;
	
	@Nullable
	@Field("id_shoper")
	@DBRef(db = "users")
	@JsonProperty("id_shoper")
	private User shoperId;
	
	@NotNull
	@Field("compras")
	@JsonProperty("products")
	private List<ShoppingCar> shoppingCar;
	
	@NotNull
	@Builder.Default
	private int estado = 0;
	
	@NotNull
	@Field("fech_compra")
	@Builder.Default
	private Date fechCompra = (@NotNull Date) new java.util.Date();
	
	@Nullable
	@Field("fech_entrega")
	@JsonProperty("fech_entrega")
	private Date fechEntrega;
	
	@Nullable
	@JsonProperty("descripcion")
	private String descripcion;
	
	@Nullable
	@JsonProperty("count")
	private Integer totalProduc;
	
	@NotNull
	private Charge charge;
	
}
