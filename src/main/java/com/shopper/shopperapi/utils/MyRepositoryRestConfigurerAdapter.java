package com.shopper.shopperapi.utils;

import com.shopper.shopperapi.models.*;
import com.shopper.shopperapi.repositories.ProductRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.MediaType;
//import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter; (extends)

@Configuration
class MyRepositoryRestConfigurerAdapter implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Shop.class, Category.class, Product.class, User.class, SubCategory.class);
        config.useHalAsDefaultJsonMediaType(false);
        config.setPageParamName("pageNumber");
//        config.setSortParamName("name");
        config.setDefaultMediaType(MediaType.APPLICATION_JSON);
        config.setDefaultPageSize(25);

        config.getCorsRegistry()
                .addMapping("/**")
                .allowedOrigins("*");;
    }
}