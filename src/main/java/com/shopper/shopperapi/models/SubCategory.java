package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "sub_categories")
public class SubCategory {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
    @DBRef
    private List<Product> products;
    @NotNull
    @Field("last_update")
    @JsonProperty("last_update")
    @DateTimeFormat
    @LastModifiedDate
    private Date lastUpdate;
}
