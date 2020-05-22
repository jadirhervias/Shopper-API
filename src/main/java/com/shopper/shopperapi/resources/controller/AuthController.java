package com.shopper.shopperapi.resources.controller;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.services.UserService;
import com.shopper.shopperapi.utils.jwt.Jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private Jwt jwt;

	private final String SECRET_KEY = "a059aee645aa166785efbf67ee6c57b0ee9f66e1afb0a1b10e3e12ed0ed8f655";

    private AuthenticationManager authenticationManager;
	
	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public HashMap<String, Object> create(@Valid @RequestBody User user) {
		user.setId(ObjectId.get());
		String id = user.getId();
		String token = getJWToken(id, user.getEmail());
		
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("user", user);
		map.put("token", token);
		
		return map;
	}
	
	@PostMapping("/login")
	public ResponseEntity<HashMap<String, Object>> login(@RequestBody String apiKeyToken){
		// TODO: si NO existe el usuario, registrarlo... obtener su id y logearlo con su token
		// user.setId(ObjectId.get());
		// String id = user.getId();
		// String token = getJWToken(id);
		
		if (apiKeyToken != null ) {

			HashMap<String, Object> map = new HashMap<>();
			
			User user = userService.findByEmail("jadir@gmail.com");
			String token = getJWToken(user.getId(), user.getEmail());
			
			map.put("user", user);
			map.put("token", token);
			
			return ResponseEntity.ok(map);
		}
		
		// return map;
		return (ResponseEntity<HashMap<String, Object>>) ResponseEntity.status(401);
	}
	
	@Bean
	private PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	private String getJWToken(String id, String email) {

		//The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		//We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// SecretKey key = Keys.secretKeyFor(signatureAlgorithm);
		String signatureString = signatureAlgorithm.toString(); // HS256

		// Build header for the jwt
		HashMap<String, Object> header = new HashMap<String, Object>();
		header.put("alg", signatureString);
		header.put("typ", "JWT");

		//Let's set the JWT Claims
		JwtBuilder tokenJWT = Jwts.builder()
					.setHeader(header)
					.setId(id)
					.setSubject(email)
					.claim("rol", "user")
					.setIssuedAt(now)
					.signWith(signingKey,signatureAlgorithm)
					// .signWith(signatureAlgorithm, signingKey);
					.setExpiration((new Date(nowMillis + 86400000)));

		// if it has been specified, let's add the expiration
		/*
		if (ttlMillis > 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		*/

		//Builds the JWT and serializes it to a compact, URL-safe string
		return tokenJWT.compact();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwt.generateToken(authRequest.getUserName());
    }
    */
	
	/*
	private void parseJWT(String jwt) {
		 
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    Claims claims = Jwts.parser()         
	       .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey.getSecret()))
	       .parseClaimsJws(jwt).getBody();
	    System.out.println("ID: " + claims.getId());
	    System.out.println("Subject: " + claims.getSubject());
	    System.out.println("Issuer: " + claims.getIssuer());
	    System.out.println("Expiration: " + claims.getExpiration());
	}
	*/
	
}
