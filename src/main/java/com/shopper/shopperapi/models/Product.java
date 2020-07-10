package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.*;

import javax.annotation.Nullable;
import java.util.Objects;

@Data
@Document(collection = "products")
@IgnoreExtraProperties
public class Product {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
    private String details;
    private double cost;
    private String format;
    private String brand;
    // Used when is in the shopping car
    @Nullable
    @com.mongodb.lang.Nullable
    private Integer quantity;
    private int stock;
    @Nullable
    @DBRef
    private Image image;
    @Nullable
//    @NotNull
    @Field("last_update")
    @JsonProperty("last_update")
    private String lastUpdate;

	public Product() {
	}

    @Nullable
    @Exclude
    public Image getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", cost=" + cost +
                ", format='" + format + '\'' +
                ", brand='" + brand + '\'' +
                ", stock=" + stock +
                ", image=" + image +
                ", lastUpdate='" + lastUpdate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.cost, cost) == 0 &&
                stock == product.stock &&
                Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(details, product.details) &&
                Objects.equals(format, product.format) &&
                Objects.equals(brand, product.brand) &&
                Objects.equals(quantity, product.quantity) &&
                Objects.equals(image, product.image) &&
                Objects.equals(lastUpdate, product.lastUpdate);
    }
}
