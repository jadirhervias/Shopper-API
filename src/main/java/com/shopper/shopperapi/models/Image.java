package com.shopper.shopperapi.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.shopper.shopperapi.services.ImageService.decompressBytes;

@Data
@Document(collection = "images")
public class Image {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @Field("source")
    private String image;

    public String getImage() {
        byte[] decodedImage = Base64.getDecoder().decode(image.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(decompressBytes(decodedImage));
    }
}
