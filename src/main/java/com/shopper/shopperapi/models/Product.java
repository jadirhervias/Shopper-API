package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private ObjectId id = ObjectId.get();
    private String name;
    private String details;
    private double cost;
    private String format;
    @Nullable
    @Indexed(unique = true)
    @Field("bar_code")
    @JsonProperty("bar_code")
    private String barCode;
    @NotNull
    @Field("last_update")
    @JsonProperty("last_update")
    private String lastUpdate;

    @Override
    public String toString() {
        return String.format(
                "Product[name='%s']", name);
    }

    public String getId() {
        return id.toHexString();
    }
}
