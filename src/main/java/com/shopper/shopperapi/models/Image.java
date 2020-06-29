package com.shopper.shopperapi.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "images")
public class Image {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @Field("source")
    private String image;
}
