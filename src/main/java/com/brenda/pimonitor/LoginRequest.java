package com.brenda.pimonitor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
	Spring Security - Login Request/Response DTOs=Data Transfer Objects
*/

public class LoginRequest {

	@NotBlank(message = "Username is required")
	@Size(min=3, max=50, message = "Username must be between 3 and 50 characters")
	@Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, underscores, and hyphens")
	private String username;


	@NotBlank(message = "Password is required")
	@Size(min = 8 , max = 100, message = "Password must be between 8 and 100 characters")
	private String password;


	public LoginRequest() {
	}

	public String getUsername(){
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password  = password;
	}
}
