package com.shopper.shopperapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//@EnableAutoConfiguration
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ShopperApiApplication {

	// @Bean para que solo se cree una instancia de cierta clase a lo largo de la aplicación. Evita la sobrecarga de instancias
	// Para usarlo, se inyecta el @Bean con @Autowired

	// Clase para hacer peticiones a otras API's
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
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
