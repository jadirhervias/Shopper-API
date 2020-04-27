package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Document(collection = "users")
@TypeAlias("user")
public class User {
    @Id
    private ObjectId id;
    @NotNull(message = "Primer nombre es requerido")
    @Field("first_name")
    @TextIndexed
    @JsonProperty("first_name")
    private String firstName;
    @NotNull(message = "Apellido es requerido")
    @Field("last_name")
    @TextIndexed
    @JsonProperty("last_name")
    private String lastName;
    @NotNull(message = "Email es requerido")
    @Email(message = "Email inválido")
    @Indexed(unique=true)
    private String email;

    public String getId() {
        return id.toHexString();
    }
}
