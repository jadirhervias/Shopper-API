package com.shopper.shopperapi.models;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "images")
public class Image {
    @Id
    private ObjectId id;
    @Field("source")
    private String image;

    public String getId() {
        return id.toHexString();
    }
}
