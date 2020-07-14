package com.shopper.shopperapi.models;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Data;
import java.util.List;
import java.util.Objects;

@Data
@IgnoreExtraProperties
public class ShoppingCar {

	@Nullable
	private String id;

	@Nullable
	private String name;

	@NotNull
	private List<Product> products;

	@NotNull
	private int count;

	@Nullable
	@JsonProperty("shop_id")
	private String shopId;

	public ShoppingCar() {
	}

	@Override
	public String toString() {
		return "ShoppingCar{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", products=" + products +
				", count=" + count +
				", shopId='" + shopId + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ShoppingCar that = (ShoppingCar) o;
		return count == that.count &&
				Objects.equals(id, that.id) &&
				Objects.equals(name, that.name) &&
				Objects.equals(products, that.products) &&
				Objects.equals(shopId, that.shopId);
	}
}
