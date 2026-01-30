package com.brenda.pimonitor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
	Security Headers ~ tells browser how to behave in order to prevent common attacks
*/

@Configuration
public class SecurityHeadersConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors (InterceptorRegistry registry) {
		registry.addInterceptor(new SecurityHeadersInterceptor());
	}

	private static class SecurityHeadersInterceptor implements HandlerInterceptor {

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
			//prevents clickjacking ~ doesnt allow site to be loaded in iframe
			response.setHeader("X-Frame-Options", "DENY");

			//prevents mime sniffing ~ browser cant guess content type
			response.setHeader("X-Content-Type-Options", "nosniff");

			//enables XSS protection in browser
			response.setHeader("X-XSS-Protection", "1; mode=block");

			//Content security policy ~ only loads resources from same origin
			response.setHeader("Content-Security-Policy",
				"default-src 'self'; " +
				"script-src 'self' 'unsafe-inline'; " +	//allows inline scripts from react
				"style-src 'self' 'unsafe-inline'; " + 	//Allows inline styles
				"img-src 'self' data:;  " +
				"font-src 'self'; " + 
				"connect-src 'self'"			//same origin cnnection
			);

			//Referrer policy - dont leak referrer info
			response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

			//permissions policy ~ disables unncessary features
			response.setHeader("Permissions-Policy",
				"geolocation=(), " +
				"microphone=(), " +
				"camera=()"
			);

			return true;
		}
	}
}
