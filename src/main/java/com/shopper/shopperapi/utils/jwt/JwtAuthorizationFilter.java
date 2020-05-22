package com.shopper.shopperapi.utils.jwt;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shopper.shopperapi.services.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final String PREFIX = "Bearer ";
	private final String SECRET = "a059aee645aa166785efbf67ee6c57b0ee9f66e1afb0a1b10e3e12ed0ed8f655";

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private Jwt jwt;

	@Autowired
	private CustomUserDetailsService service;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
//			String HEADER = "Authorization";
//			String bearerToken = request.getHeader(HEADER);
//			logger.info("Bearer token: " + bearerToken);
//			String token = request.getHeader(HEADER).substring(7);
//			String email = jwt.extractEmail(token);
//			logger.info("Usuario email: " + email);
//
//			// String token = request.getHeader(HEADER);
//			// String email = jwt.extractEmail(token);
//
//			logger.info("checking authentication for user " + email);
//
//			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//				UserDetails userDetails = service.loadUserByUsername(email);
//
//				if (jwt.validateToken(token, userDetails)) {
//
//					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//					usernamePasswordAuthenticationToken
//							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//					logger.info("authenticated user " + email + ", setting security context");
//
//					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//				}
//
//			}
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			(response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
			return;
		}
	}
}