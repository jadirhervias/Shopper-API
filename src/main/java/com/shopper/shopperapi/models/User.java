package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
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
import java.util.Map;

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
    @Indexed(unique = true)
    private String email;

//    TODO : Default role is CUSTOMER
//    @Value("${role: ROLE_CUSTOMER}")
    @Field("role")
    @JsonProperty("role")
    private String role = "ROLE_CUSTOMER"; 
    
    @NotNull(message = "La contraseña es requerida")
    @Field("password")
    @JsonProperty("password")
    private String password;
    
    @Field("phone_number")
    @JsonProperty("phone_number")
    @NotNull(message = "El teléfono es requerido")
    private String phoneNumber;
    
    @Field("address")
    @JsonProperty("address")
    @NotNull(message = "La dirección es requerida")
    private String address;

    @Field("address_lat")
    @JsonProperty("user_lat")
    @NotNull
    private double userLat;

    @Field("address_lng")
    @JsonProperty("user_lng")
    @NotNull
    private double userLng;

    @Nullable
    @Field("notification_device_group")
    @JsonProperty("notification_device_group")
    private Map<String, String> notificationDeviceGroup;

	public User (){
	}
    
    public String getId() {
        return id.toHexString();
    }

//    public ObjectId getObjectId() {
//        return id;
//    }

    public void setUserNotificationKey(String userNotificationKey) {
        this.notificationDeviceGroup.replace("notification_key", userNotificationKey);
    }

    public void setUserNotificationKeyName(String userNotificationKeyName) {
        this.notificationDeviceGroup.replace("notification_key_name", userNotificationKeyName);
    }
}
