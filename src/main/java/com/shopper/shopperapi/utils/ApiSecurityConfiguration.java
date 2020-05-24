package com.shopper.shopperapi.utils;

import com.shopper.shopperapi.utils.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import static com.shopper.shopperapi.utils.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApiSecurityConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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
            .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
            .antMatchers("api/v1/users/*").hasRole(ADMIN.name())
            .antMatchers("/api/v1/auth/login", "/api/v1/auth/register")
                .permitAll()
//            .antMatchers("/api/v1/users").hasRole(ApplicationUserRole.ADMIN.name())
//            .antMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority(ApplicationUserPermission.USERS_READ.getPermission())
//            .antMatchers(HttpMethod.GET, "/api/v1/products").hasAuthority(ApplicationUserPermission.PRODUCTS_READ.getPermission())
            .anyRequest()
                .authenticated()
            .and()
            .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();

        UserDetails shopper = User.builder()
                .username("shopper")
                .password(passwordEncoder.encode("shopper"))
                .authorities(ApplicationUserRole.SHOPPER.getGrantedAuthorities())
                .build();

        UserDetails customer = User.builder()
                .username("customer")
                .password(passwordEncoder.encode("customer"))
                .authorities(ApplicationUserRole.CUSTOMER.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(admin, shopper, customer);
    }
}
