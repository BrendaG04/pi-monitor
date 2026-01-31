package com.brenda.pimonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
	Spring Security ~  Configuration, specifying which endpoints need auth and which are public
*/

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// Enables CORS
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			//Disable CSRF
			.csrf(csrf -> csrf.disable())
			//Config auth rules
			.authorizeHttpRequests(auth -> auth
				//public endpoints that dont need auth
				.requestMatchers("/auth/**").permitAll()
				//All other endpoints require auth
				.anyRequest().authenticated()
			)

			//Stateless session (Jwt doesnt need server-side sessions)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			//Add jwt filter before sprigns default auth filter
			.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
    	public CorsConfigurationSource corsConfigurationSource() {
        	CorsConfiguration configuration = new CorsConfiguration();

		String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
		if (allowedOrigins != null) {
			configuration.setAllowedOrigins(java.util.Arrays.asList(allowedOrigins.split(",")));
		}else {
			configuration.setAllowedOrigins(java.util.Arrays.asList(
				"http://localhost:3000",
				"http://localhost"
			));
		}


        	configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        	configuration.setAllowedHeaders(java.util.Arrays.asList("*"));
        	configuration.setAllowCredentials(true);
        	configuration.setExposedHeaders(java.util.Arrays.asList("Authorization"));

        	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        	source.registerCorsConfiguration("/**", configuration);
       	 	return source;
    }
}
