package com.shopper.shopperapi.utils;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("user").password("user").roles("USER").and()
            .withUser("admin").password("admin").roles("USER", "ADMIN");
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

    @Configuration
    @Order(1)
    public static class ApiKeyTokenSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Value("${shopper.http.api-key-token}")
        private String apiKeyTokenValue;

        @Value("${shopper.http.api-key-token-parameter}")
        private String apiKeyTokenRequestParameter;

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            ApiKeyTokenAuthFilter filter = new ApiKeyTokenAuthFilter(apiKeyTokenRequestParameter);
            filter.setAuthenticationManager(new AuthenticationManager() {
                @Override
                public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                    String principal = (String) authentication.getPrincipal();
                    if (!apiKeyTokenValue.equals(principal)) {
                                       
                        throw new BadCredentialsException("El API key token no fue encontrado o es inválido");
                    }
                    authentication.setAuthenticated(true);
                    return authentication;
                }
            });
            httpSecurity.
                    antMatcher("/**").
                    csrf().disable().
                    sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                    and().
                    addFilter(filter).authorizeRequests().anyRequest().authenticated();
        }
    }

//    @Configuration
//    @Order(2)
//    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                .antMatcher("/api/**")
//                .authorizeRequests()
//                .anyRequest().hasRole("ADMIN")
//                .and()
//                .httpBasic();
//        }
//    }

    // Basic Authentication
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // Se define la clase que recupera los usuarios y el algoritmo para procesar las passwords
//        auth.userDetailsService(customUserDetailsService).passwordEncoder(bcryptPasswordEncoder());
//
//    }

    // Authorization
    // No @Order(): último por defecto
    @Order(2)
    @Configuration
    public static class BasicAuthSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                .antMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated().and()
                .csrf().disable()
                .formLogin();
        }
    }
}
