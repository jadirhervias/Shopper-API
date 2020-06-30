package com.shopper.shopperapi.models;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
import java.util.Objects;

@Data
@Document(collection = "shopping_cars")
@IgnoreExtraProperties
public class ShoppingCar {

	@Nullable
	@MongoId(FieldType.OBJECT_ID)
	private String id;

	@NotNull
	@DBRef
	private List<Product> products;

	@NotNull
	private int count;

	public ShoppingCar() {
	}

	@Override
	public String toString() {
		return "ShoppingCar{" +
				"id=" + id +
				", products=" + products +
				", count=" + count +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ShoppingCar)) return false;
		ShoppingCar that = (ShoppingCar) o;
		return getCount() == that.getCount() &&
				Objects.equals(getId(), that.getId()) &&
				Objects.equals(getProducts(), that.getProducts());
	}
}
