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
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Double.compare(product.getCost(), getCost()) == 0 &&
                getStock() == product.getStock() &&
                Objects.equals(getId(), product.getId()) &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getDetails(), product.getDetails()) &&
                Objects.equals(getFormat(), product.getFormat()) &&
                Objects.equals(getBrand(), product.getBrand()) &&
                Objects.equals(getImage(), product.getImage()) &&
                Objects.equals(getLastUpdate(), product.getLastUpdate());
    }
}
