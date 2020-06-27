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
@Document(collection = "purchases")
public class Order {

	@MongoId(FieldType.OBJECT_ID)
	private ObjectId id;
	
	@NotNull
	@Field("id_user")
	@DBRef(db = "users")
	@JsonProperty("id_user")
	private User userId;

	@Transient
	@JsonProperty("firebase_db_reference_key")
	private String firebaseDbReferenceKey;
	
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
//	@Builder
	private int estado = 0;
	
	@NotNull
	@Field("fech_compra")
//	@Builder
	private Date fechCompra;
//	= (@NotNull Date) new java.util.Date();
	
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

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public User getShoperId() {
		return shoperId;
	}

	public void setShoperId(User shoperId) {
		this.shoperId = shoperId;
	}

	public List<ShoppingCar> getShoppingCar() {
		return shoppingCar;
	}

	public void setShoppingCar(List<ShoppingCar> shoppingCar) {
		this.shoppingCar = shoppingCar;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public Date getFechCompra() {
		return fechCompra;
	}

	public void setFechCompra(Date fechCompra) {
		this.fechCompra = fechCompra;
	}

	public Date getFechEntrega() {
		return fechEntrega;
	}

	public void setFechEntrega(Date fechEntrega) {
		this.fechEntrega = fechEntrega;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getTotalProduc() {
		return totalProduc;
	}

	public void setTotalProduc(Integer totalProduc) {
		this.totalProduc = totalProduc;
	}

	public Charge getCharge() {
		return charge;
	}

	public void setCharge(Charge charge) {
		this.charge = charge;
	}

}
