package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.mongodb.lang.Nullable;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Map;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Document(collection = "users")
@IgnoreExtraProperties
@TypeAlias("user")
public class User {
    // Valid hex string
    @MongoId(FieldType.OBJECT_ID)
    private String id;

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
    private String role = "ROLE_CUSTOMER";

    @NotNull(message = "La contraseña es requerida")
    @Exclude
    private String password;

    @Field("phone_number")
    @JsonProperty("phone_number")
    @NotNull(message = "El teléfono es requerido")
    private String phoneNumber;

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

    public User() {
    }

    // CUSTOM GETTERS

    @Exclude
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", phone_number='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", user_lat=" + userLat +
                ", user_lng=" + userLng +
                ", notification_device_group=" + notificationDeviceGroup +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Double.compare(user.userLat, userLat) == 0 &&
                Double.compare(user.userLng, userLng) == 0 &&
                Objects.equals(id, user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(role, user.role) &&
                Objects.equals(password, user.password) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(address, user.address) &&
                Objects.equals(notificationDeviceGroup, user.notificationDeviceGroup);
    }
}
