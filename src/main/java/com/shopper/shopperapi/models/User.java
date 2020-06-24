package com.shopper.shopperapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Value;
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
    @Indexed(unique = true)
    private String email;
    
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
    private double user_lat;

    @Field("address_lng")
    @JsonProperty("user_lng")
    @NotNull
    private double user_lng;
    
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

	public double getUser_lat() {
		return user_lat;
	}

	public void setUser_lat(double user_lat) {
		this.user_lat = user_lat;
	}

	public double getUser_lng() {
		return user_lng;
	}

	public void setUser_lng(double user_lng) {
		this.user_lng = user_lng;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public ObjectId idShop() {
		return id;
	}
    
    
}
