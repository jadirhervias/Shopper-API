package com.shopper.shopperapi.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Data
@Document(collection = "shops")
public class Shop {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String name;

    @NotNull
    @Field("last_update")
    @JsonProperty("last_update")
    @DateTimeFormat
    @LastModifiedDate
    private Date lastUpdate;

    @DBRef
    private List<Category> categories;

    @Nullable
    private String image;

    @NotNull
    @Field("shop_lat")
    @JsonProperty("shop_lat")
    private double shopLat;

    @NotNull
    @Field("shop_lng")
    @JsonProperty("shop_lng")
    private double shopLng;
}
