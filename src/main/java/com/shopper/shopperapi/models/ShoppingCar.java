package com.shopper.shopperapi.models;

import javax.validation.constraints.NotNull;

import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Data;
import java.util.List;
import java.util.Objects;

@Data
@IgnoreExtraProperties
public class ShoppingCar {

	@NotNull
	private List<Product> products;

	@NotNull
	private int count;

	public ShoppingCar() {
	}

	@Override
	public String toString() {
		return "ShoppingCar{" +
				", products=" + products +
				", count=" + count +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ShoppingCar that = (ShoppingCar) o;
		return count == that.count &&
				Objects.equals(products, that.products);
	}
}
