package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import com.mongodb.lang.Nullable;
import lombok.Data;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.repository.Query;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Base64;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private ObjectId id = ObjectId.get();
    private String name;
    private String details;
    private double cost;
    private String format;
    private String brand;
    private int stock;
    @DBRef
    private Image image;
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
