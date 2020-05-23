package com.shopper.shopperapi.utils;

import com.shopper.shopperapi.utils.security.ApplicationUserPermission;
import com.shopper.shopperapi.utils.security.ApplicationUserRole;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//            .inMemoryAuthentication()
//            .withUser("user").password(bcryptPasswordEncoder().encode("user")).roles("USER").and()
//            .withUser("admin").password(bcryptPasswordEncoder().encode("admin")).roles("USER", "ADMIN");
//    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

//    @Configuration
//    @Order(1)
//    public static class ApiKeyTokenSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//        @Value("${shopper.http.api-key-token}")
//        private String apiKeyTokenValue;
//
//        @Value("${shopper.http.api-key-token-parameter}")
//        private String apiKeyTokenRequestParameter;
//
//        @Override
//        protected void configure(HttpSecurity httpSecurity) throws Exception {
//            ApiKeyTokenAuthFilter filter = new ApiKeyTokenAuthFilter(apiKeyTokenRequestParameter);
//            filter.setAuthenticationManager(new AuthenticationManager() {
//                @Override
//                public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                    // Obtener info del usuario logeado
//                    String principal = (String) authentication.getPrincipal();
//                    final Log logger = LogFactory.getLog(this.getClass());
//
//                    if (!apiKeyTokenValue.equals(principal)) {
//                        logger.info("Api token no es igual al del entorno - filter response");
//                        throw new BadCredentialsException("El API key token no fue encontrado o es inválido");
//                    }
//
//                    logger.info("Api token OK (" + apiKeyTokenValue + ") - filter response");
//                    authentication.setAuthenticated(true);
//                    return authentication;
//                }
//            });
//            httpSecurity.
//                    antMatcher("/api/v1/**")
//                    .csrf()
//                        .disable()
//                    .sessionManagement()
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
//                    .addFilter(filter)
//                    .authorizeRequests()
//                        .anyRequest()
//                        .authenticated();
//        }
//    }

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
//    @Order(2)
//    @Configuration
//    public static class BasicAuthSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                .authorizeRequests()
////                .antMatchers("/api/v1/**")
////                    .hasRole("ADMIN")
//                .anyRequest()
//                    .authenticated()
//                .and()
//                .csrf()
//                    .disable()
//                .httpBasic();
//        }
//    }

    // Authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable()
            .formLogin()
                .disable()
            .authorizeRequests()
            .antMatchers("/api/v1/auth/login", "/api/v1/auth/register")
                .permitAll()
//            .antMatchers("/api/v1/users").hasRole(ApplicationUserRole.ADMIN.name())
//            .antMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority(ApplicationUserPermission.USERS_READ.getPermission())
//            .antMatchers(HttpMethod.GET, "/api/v1/products").hasAuthority(ApplicationUserPermission.PRODUCTS_READ.getPermission())
            .anyRequest()
                .authenticated()
            .and()
            .httpBasic();
//            .and()
//            .rememberMe()
//                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
//                .key("algoseguro");
//            .and()
//            .logout()
//                .logoutUrl("/")
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID", "remember-me")
//                .logoutSuccessUrl("/");
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(bcryptPasswordEncoder().encode("admin"))
//                .roles(ApplicationUserRole.ADMIN.name()) // ROLE_ADMIN
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();

        UserDetails shopper = User.builder()
                .username("shopper")
                .password(bcryptPasswordEncoder().encode("shopper"))
//                .roles(ApplicationUserRole.ADMIN.name()) // ROLE_SHOPPER
                .authorities(ApplicationUserRole.SHOPPER.getGrantedAuthorities())
                .build();

        UserDetails customer = User.builder()
                .username("customer")
                .password(bcryptPasswordEncoder().encode("customer"))
//                .roles(ApplicationUserRole.CUSTOMER.name()) // ROLE_CUSTOMER
                .authorities(ApplicationUserRole.CUSTOMER.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(admin, shopper, customer);
    }
}
