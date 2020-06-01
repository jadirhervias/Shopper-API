package com.shopper.shopperapi.utils.jwt;

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

public class JwtUsernameAndPasswordAuthenticationFilter extends GenericFilterBean {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final ApiKeyTokenVerifier apiKeyTokenVerifier;
    private final JSONObject userJson = new JSONObject();

    public JwtUsernameAndPasswordAuthenticationFilter(
            ApiKeyTokenVerifier apiKeyTokenVerifier,
            JwtConfig jwtConfig,
            SecretKey secretKey
    ) {
        super();
        this.apiKeyTokenVerifier = apiKeyTokenVerifier;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
//            response.reset();
        response.setContentType("application/json");

        // Requiere acceso al método POST
//            apiKeyTokenRequest.setApiKeyToken(Arrays.toString(request.getParameterMap().get("api_key_token")));


        // PARA EVITAR QUE PIDA API_KEY_TOKEN EN CADA REQUEST

        String header = httpRequest.getHeader("Authorization");

        if(header != null && !header.startsWith("Bearer ")) {
            if (!header.startsWith("Basic ")) {
//                ApiResponse apiResponse = new ApiResponse(401, "Parametros Requeridos", false);
//                OutputStream outputStream = response.getOutputStream();
//                ObjectMapper mapper = new ObjectMapper();
//                mapper.writeValue(outputStream, apiResponse);
//                outputStream.flush();

                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Credenciales de autenticación requeridas");

            }else if (header.startsWith("Basic ")) {
                ArrayList<String> headers = Collections.list(httpRequest.getHeaderNames());

                boolean apiKeyTokenExists = false;

                for (String key : headers) {
                    System.out.println(key);
                    if (key.equals("api_key_token")) {
                        apiKeyTokenExists = true;
                        break;
                    }
                }

                System.out.println(">>>>>>>>>>> API KEY tokennnnn: " + apiKeyTokenExists);
                if (!apiKeyTokenExists) {

//                    ApiResponse apiResponse = new ApiResponse(401, "API KEY TOKEN REQUERIDO", false);
//                    OutputStream outputStream = response.getOutputStream();
//                    ObjectMapper mapper = new ObjectMapper();
//                    mapper.writeValue(outputStream, apiResponse);
//                    outputStream.flush();

                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                            "API key token requerido");

                } else {
                    System.out.println(">>>>>>>>>>> API KEY EXISTE");

                    if (!httpRequest.getHeader("api_key_token").isEmpty()) {
                        String apiKeyToken = httpRequest.getHeader("api_key_token");
                        ApiKeyTokenRequest apiKeyTokenRequest = new ApiKeyTokenRequest();
                        apiKeyTokenRequest.setApiKeyToken(apiKeyToken);
                        boolean isValid = apiKeyTokenVerifier.verifyApiKeyToken(apiKeyTokenRequest.getApiKeyToken());

                        if (!isValid) {
                            System.out.println(">>>>>>>>>>> API KEY INVALIDA");

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

                        this.userJson.put("email", authResult.getName());

                        json.put("token", token);
                        json.put("user", userJson);

                        try {
                            PrintWriter out = response.getWriter();
                            //ServletOutputStream out = response.getOutputStream();
                            out.println(json.toString());
                            out.flush();

                            filterChain.doFilter(request, response);
                        } catch (Exception ignored) {

                        }
                    }

                }
//                else {
//                    HttpServletResponse httpResponse = (HttpServletResponse) response;
//                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
//                            "Api key token is invalid or incorrect");
//                }
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }
}