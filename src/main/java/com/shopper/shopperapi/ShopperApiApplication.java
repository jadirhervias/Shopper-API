package com.shopper.shopperapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//@EnableAutoConfiguration
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
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
