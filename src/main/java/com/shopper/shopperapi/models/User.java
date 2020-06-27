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

import java.util.Map;

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
    
    public String getId() {
        return id.toHexString();
    }

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}
	
	public Map<String, String> getNotificationDeviceGroup() {
		return notificationDeviceGroup;
	}

	public void setNotificationDeviceGroup(Map<String, String> notificationDeviceGroup) {
		this.notificationDeviceGroup = notificationDeviceGroup;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getUserLat() {
		return userLat;
	}

	public void setUserLat(double userLat) {
		this.userLat = userLat;
	}

	public double getUserLng() {
		return userLng;
	}

	public void setUserLng(double userLng) {
		this.userLng = userLng;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public void setUserNotificationKey(String userNotificationKey) {
        this.notificationDeviceGroup.replace("notification_key", userNotificationKey);
    }
	
	public void setUserNotificationKeyName(String userNotificationKeyName) {
        this.notificationDeviceGroup.replace("notification_key_name", userNotificationKeyName);
    }
}
