package com.shopper.shopperapi.utils;

import com.shopper.shopperapi.auth.ApplicationUserService;
import com.shopper.shopperapi.services.UserService;
import com.shopper.shopperapi.utils.apiKeyToken.ApiKeyTokenVerifier;
import com.shopper.shopperapi.utils.jwt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;

import static com.shopper.shopperapi.utils.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final UserService userService;
    private final ApiKeyTokenVerifier apiKeyTokenVerifier;

    @Autowired
    public ApiSecurityConfiguration(PasswordEncoder passwordEncoder,
                                    ApplicationUserService applicationUserService,
                                    SecretKey secretKey,
                                    JwtConfig jwtConfig,
                                    ApiKeyTokenVerifier apiKeyTokenVerifier,
                                    UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.apiKeyTokenVerifier = apiKeyTokenVerifier;
        this.userService = userService;
    }

    // Authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
            .csrf().disable()
            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // Registrar por mientras
                .mvcMatchers(HttpMethod.POST, "users").permitAll()
                .mvcMatchers(HttpMethod.GET, "api/v1/shops/**").permitAll()
                .antMatchers("/", "/login", "swagger-ui.html").permitAll()
            .anyRequest()
                .authenticated()
            .and()
            .httpBasic()
//                .authenticationEntryPoint()
            .and()
            .addFilterAfter(new JwtUsernameAndPasswordAuthenticationFilter(apiKeyTokenVerifier, jwtConfig, secretKey, userService), BasicAuthenticationFilter.class)
            .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class);
    }

    // Authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }
}
