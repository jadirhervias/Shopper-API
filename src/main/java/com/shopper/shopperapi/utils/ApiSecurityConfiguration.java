package com.shopper.shopperapi.utils;

import com.shopper.shopperapi.auth.ApplicationUserService;
import com.shopper.shopperapi.utils.apiKeyToken.ApiKeyTokenVerifier;
import com.shopper.shopperapi.utils.jwt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private final ApiKeyTokenVerifier apiKeyTokenVerifier;

    @Autowired
    public ApiSecurityConfiguration(PasswordEncoder passwordEncoder,
                                    ApplicationUserService applicationUserService,
                                    SecretKey secretKey,
                                    JwtConfig jwtConfig,
                                    ApiKeyTokenVerifier apiKeyTokenVerifier) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.apiKeyTokenVerifier = apiKeyTokenVerifier;
    }

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

    // Authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
//            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/", "index", "/css/*", "/js/*", "/api/v1/auth/login", "/api/v1/auth/register")
                .permitAll()
            .antMatchers("api/v1/users/*").hasRole(ADMIN.name())
            .anyRequest()
                .authenticated()
            .and()
            .httpBasic()
//                .authenticationEntryPoint()
            .and()
//            .addFilterBefore(new ApiKeyTokenFilter(apiKeyTokenVerifier), BasicAuthenticationFilter.class)
//            .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
            .addFilterAfter(new JwtUsernameAndPasswordAuthenticationFilter(apiKeyTokenVerifier, jwtConfig, secretKey), BasicAuthenticationFilter.class)
            .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
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
