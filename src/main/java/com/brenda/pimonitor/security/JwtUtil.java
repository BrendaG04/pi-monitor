package com.brenda.pimonitor.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
	Spring Security ~ JWT Utility class / Generates and validates JWT tokens
*/


@Component
public class JwtUtil {

	//Secret key for signing tokens
	private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	//Token validity  ~ 24 hrs
	private final long EXPIRATION_TIME = 1000 * 60 * 60 *24;

	//Generates JWT Tokens for a user
	public String generateToken(String username){
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}

	//Creates the actual token
	private String createToken( Map<String, Object> claims , String subject) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() * EXPIRATION_TIME);

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(subject)
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(SECRET_KEY)
			.compact();
	}

	//Extracts username from token
	public String extractUsername (String token) {
		return extractAllClaims(token).getSubject();
	}

	//Extract expiration date
	public Date extractExpiration( String token){
		return extractAllClaims(token).getExpiration();
	}

	//Parses all claims from token
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(SECRET_KEY)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	//Checks if token is expired
	private Boolean isTokenExpired (String token) {
		return extractExpiration(token).before(new Date());
	}

	//Validates token
	public Boolean validateToken(String token, String username) {
		final String extractedUsername = extractUsername(token);
		return (extractedUsername.equals(username) && !isTokenExpired(token));
	}
}

