package com.shopper.shopperapi.utils.jwt;

import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.services.UserService;
import com.shopper.shopperapi.utils.apiKeyToken.ApiKeyTokenRequest;
import com.shopper.shopperapi.utils.apiKeyToken.ApiKeyTokenVerifier;

import io.jsonwebtoken.Jwts;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.crypto.SecretKey;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

public class JwtUsernameAndPasswordAuthenticationFilter extends GenericFilterBean {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final ApiKeyTokenVerifier apiKeyTokenVerifier;
    private final UserService userService;
    private final JSONObject userJson = new JSONObject();

    public JwtUsernameAndPasswordAuthenticationFilter(
            ApiKeyTokenVerifier apiKeyTokenVerifier,
            JwtConfig jwtConfig,
            SecretKey secretKey,
            UserService userService
    ) {
        super();
        this.apiKeyTokenVerifier = apiKeyTokenVerifier;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.userService = userService;
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    	
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        response.setContentType("application/json");

        String header = httpRequest.getHeader("Authorization");

        if(header != null && !header.startsWith("Bearer ")) {
            if (!header.startsWith("Basic ")) {

                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Credenciales de autenticación requeridas");

            }else if (header.startsWith("Basic ")) {
                ArrayList<String> headers = Collections.list(httpRequest.getHeaderNames());

                boolean apiKeyTokenExists = false;

                for (String key : headers) {
                    if (key.equals("api_key_token")) {
                        apiKeyTokenExists = true;
                        break;
                    }
                }
                if (!apiKeyTokenExists) {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                            "API key token requerido");

                } else {

                    if (!httpRequest.getHeader("api_key_token").isEmpty()) {
                        String apiKeyToken = httpRequest.getHeader("api_key_token");
                        ApiKeyTokenRequest apiKeyTokenRequest = new ApiKeyTokenRequest();
                        apiKeyTokenRequest.setApiKeyToken(apiKeyToken);
                        boolean isValid = apiKeyTokenVerifier.verifyApiKeyToken(apiKeyTokenRequest.getApiKeyToken());

                        if (!isValid) {
                            HttpServletResponse httpResponse = (HttpServletResponse) response;
                            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                    "Api key token is invalid or incorrect");
                            return;
                        }

                        SecurityContext sc = SecurityContextHolder.getContext();
                        Authentication authResult = sc.getAuthentication();

                        String token = Jwts.builder()
                                .setSubject(authResult.getName())
                                .claim("authorities", authResult.getAuthorities())
                                .setIssuedAt(new Date())
                                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                                .signWith(secretKey)
                                .compact();

                        JSONObject json = new JSONObject();
                        userJson.put("email", authResult.getName());
                        Optional<User> user = this.usuario(authResult.getName());
                        userJson.put("id", user.get().getId());
                        userJson.put("email", user.get().getEmail());
                        userJson.put("first_name", user.get().getFirstName());
                        userJson.put("last_name", user.get().getLastName());
                        userJson.put("addres", user.get().getAddress());
                        userJson.put("user_lat", user.get().getUserLat());
                        userJson.put("user_lng", user.get().getUserLng());
                        userJson.put("phone_number", user.get().getPhoneNumber());
                        userJson.put("notification", user.get().getNotificationDeviceGroup());
                        json.put("token", token);
                        json.put("user", userJson);

                        try {
                            PrintWriter out = response.getWriter();
                            out.println(json.toString());
                            out.flush();

                            filterChain.doFilter(request, response);
                        } catch (Exception ignored) {

                        }
                    }

                }
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }
    
    public Optional<User> usuario(String email) {
    	return this.userService.findByEmail(email);
    }
}