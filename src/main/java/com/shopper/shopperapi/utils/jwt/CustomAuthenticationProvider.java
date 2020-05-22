package com.shopper.shopperapi.utils.jwt;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.services.UserService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        final Log logger = LogFactory.getLog(this.getClass());

        boolean notProductionProfile = env.acceptsProfiles(Profiles.of("default", "homolog"));

        String username = auth.getName();
        String password = auth.getCredentials().toString();

        if (notProductionProfile && "admin".equals(username) && "admin".equals(password)) {
            return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("protected")));
        } else {
        	
            // boolean usernameActiveExists = userService.userExist(username);
        	User user = userService.findByEmail(username);

            // if (usernameActiveExists) {
        	if (user != null) {
        		// User user = userService.findByEmail(username);

                String hashPassword = user.getPassword();

                boolean matches = passwordEncoder.matches(password, hashPassword);

                if (matches) {
                    logger.info("Credenciales - user: " + username + "password: " + hashPassword + " válidas!");
                    return new UsernamePasswordAuthenticationToken(username, hashPassword, Arrays.asList(new SimpleGrantedAuthority(user.getRole().toString()), new SimpleGrantedAuthority("protected")));
                } else {
                    throw new BadCredentialsException("Credenciales inválidas");
                }
            }
        }
        throw new UsernameNotFoundException( "Credenciales inválidas" );

    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals( UsernamePasswordAuthenticationToken.class );
    }
}
