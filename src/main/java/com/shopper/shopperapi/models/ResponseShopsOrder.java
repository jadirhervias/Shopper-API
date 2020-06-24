package com.shopper.shopperapi.models;

import org.springframework.data.mongodb.core.mapping.DBRef;
import com.mongodb.lang.Nullable;
import lombok.Data;

@Data
public class ResponseShopsOrder{
	
	@Nullable
	private Double distancia;
	
	@DBRef(db = "shops")
	private Shop shop;
	
	public double getDistancia() {
		return distancia;
	}
	public ResponseShopsOrder(double distancia, Shop shop) {
		super();
		this.distancia = distancia;
		this.shop = shop;
	}
	
	@Override
	public String toString() {
		return String.format("ResponseShopsOrder[distancia='%s',shops='%s']",distancia,shop);
	}
	
	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
}
