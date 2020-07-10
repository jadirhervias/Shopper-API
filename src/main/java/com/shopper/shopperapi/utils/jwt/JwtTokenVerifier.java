package com.shopper.shopperapi.utils.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Filtro que se ejecuta una vez en cada request después del login
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwtTokenVerifier(SecretKey secretKey,
                            JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Obtener el token del header Authorization
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Quitar el Bearer
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

        System.out.println(">>>>>>>>>>>> token a parsear: " + token);

        try {

            // Validar el JWT
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            // Obtener info del usuario
            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            // Obtener los permisos firmados en el JWT del usuario
            var authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            // Autenticar el token
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );

            // Establecer el contexto de usuario en la aplicación como autenticado
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Api key token is invalid or incorrect");
//            throw new IllegalStateException(String.format("Token %s inválido", token));
        }

        // para pasar el request y response al siguiente filter (como next() en Express)
        filterChain.doFilter(request, response);
    }
}
