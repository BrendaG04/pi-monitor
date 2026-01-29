package com.brenda.pimonitor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
	Spring Security ~ Filter runs on every request and checks if JWt token is valid
*/


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthService authService;

	@Override
	protected void doFilterInternal (HttpServletRequest request,HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

		//Get Auth header
		final String authorizationHeader = request.getHeader("Authorization");

		String username = null;
		String jwt = null;

		//Extract jwt from header
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			try {
				username = jwtUtil.extractUsername(jwt);
			} catch (Exception e) {
				System.out.println("Unable to extract username from token");
			}
		}

		//In case of a username but no auth
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			User user = authService.getUser(username);
			if ( user != null && jwtUtil.validateToken(jwt, username)) {
				UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
				UserDetails userDetails = userBuilder
						.password(user.getPassword())
						.authorities("ROLE_" + user.getRole())
						.build();

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken (
											userDetails,
											null,
											userDetails.getAuthorities()
										);

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}
