package com.shopper.shopperapi.utils;

import com.shopper.shopperapi.utils.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.shopper.shopperapi.services.CustomUserDetailsService;
import com.shopper.shopperapi.utils.jwt.CustomAuthenticationProvider;

// import org.springframework.web.filter.CorsFilter;
// import com.shopper.shopperapi.utils.jwt.CorsFilter;

import com.shopper.shopperapi.utils.jwt.JwtAuthorizationFilter;
import com.shopper.shopperapi.utils.middlewares.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private RestAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	// @Autowired
	// private JwtAuthorizationFilter jwtAuthorizationFilter;

	// @Autowired
	// private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomAuthenticationProvider customAuthProvider;

    // @Autowired
    // private CorsFilter corsFilter;
	
	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
          .inMemoryAuthentication()
          .withUser("admin")
          .password(bcryptPasswordEncoder().encode("admin"))
          // .authorities("ROLE_USER")
          .authorities("ROLE_ADMIN");
    }
	*/
	
	protected void configure(HttpSecurity http) throws Exception {
		// No need for CSRF because the token is invulnerable
		http.csrf().disable();

		http.headers().frameOptions().disable();
		http.formLogin().disable();
		http.httpBasic().authenticationEntryPoint(authenticationEntryPoint);
		http.authenticationProvider(customAuthProvider);

		http.cors().and()
				.authorizeRequests()
				//.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				// allow anonymous resource requests
//				.antMatchers(
//						HttpMethod.GET,
//						"/",
//						"/*.html",
//						"/favicon.ico",
//						"/**/*.html",
//						"/**/*.css",
//						"/**/*.js"
//				).permitAll()

				.antMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/v1/users").permitAll()
				.anyRequest().authenticated().and()
				.exceptionHandling().and()
				// don't create session
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Custom JWT based security filter
		// http.addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
		// http.addFilter(new JWTAuthenticationFilter(authenticationManager()))

		// disable page caching or add some defautl heraders
		http.headers().cacheControl();




		//.addFilterAfter(new JwtAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
		// http.addFilterBefore(corsFilter, SessionManagementFilter.class);
		
		// http.addFilterAfter(new CustomFilter(), BasicAuthenticationFilter.class);
		
		// .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
		
			// .and()
			// .addFilter(new JWTAuthenticationFilter(authenticationManager()))
			// .addFilter(new JWTAuthorizationFilter(authenticationManager()));
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Se define la clase que recupera los usuarios y el algoritmo para procesar las passwords
		auth.userDetailsService(customUserDetailsService).passwordEncoder(bcryptPasswordEncoder());

	}
	
//	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//		return (CorsConfigurationSource) source;
//	}
	
	
	// .httpBasic()
	/*
	httpSecurity.httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	.and().cors().and()
	.csrf().disable().formLogin().disable().authorizeRequests()
	.antMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
	.antMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
	.anyRequest()
	.authenticated().and()
	.addFilter(new JWTAuthenticationFilter(authenticationManager()))
	.addFilter(new JWTAuthorizationFilter(authenticationManager()));
	*/
}
