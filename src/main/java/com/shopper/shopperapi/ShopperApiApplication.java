package com.shopper.shopperapi;

import com.google.firebase.database.FirebaseDatabase;
import com.shopper.shopperapi.repositories.UserRepository;
import com.shopper.shopperapi.utils.apiKeyToken.ApiKeyTokenConfig;
import com.shopper.shopperapi.utils.jwt.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@EnableConfigurationProperties({JwtConfig.class, ApiKeyTokenConfig.class})
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
