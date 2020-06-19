package com.shopper.shopperapi.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

// Para construir un POJO (getters y setters)
// @Document annotation is used for the same purpose with @Entity annotation in JPA
@Data
@Document(collection = "shops")
public class Shop {
//    @Id
    @MongoId(FieldType.OBJECT_ID)
    private ObjectId id;
    private String name;
    @NotNull
    @Field("last_update")
    @JsonProperty("last_update")
    @DateTimeFormat
    @LastModifiedDate
    private Date lastUpdate;

    @DBRef
    private List<Category> categories;

    public String getId() {
        return id.toHexString();
    }

    @Override
    public String toString() {
        return String.format(
            "Shop[id=%s, lastUpdate='%s']",
            id, lastUpdate
        );
    }
}
