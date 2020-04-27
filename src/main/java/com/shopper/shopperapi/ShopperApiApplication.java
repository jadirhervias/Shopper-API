package com.shopper.shopperapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableAutoConfiguration
@SpringBootApplication
public class ShopperApiApplication {

/*
	@Bean
	public ValidatingMongoEventListener validatingMongoEventListener() {
		return new ValidatingMongoEventListener(validator());
	}
*/

/*
	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}
*/

	public static void main(String[] args) {
		SpringApplication.run(ShopperApiApplication.class, args);
	}

}
