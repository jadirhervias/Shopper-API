package com.shopper.shopperapi.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@Document(collection = "categories")
public class Category {
	@MongoId(FieldType.OBJECT_ID)
	private String id;
    private String name;
    @Field("sub_categories")
    @JsonProperty("sub_categories")
    @DBRef
    private List<SubCategory> subCategories;

    @Override
    public String toString() {
        return String.format("Category[name='%s']", name);
    }
}
