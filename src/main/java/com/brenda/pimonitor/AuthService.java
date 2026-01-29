package com.brenda.pimonitor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
	Spring Security ~ Auth service class / handles login logic
*/

@Service
public class AuthService {

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	//No database for now, im using in memory user storage ~ map
	private final Map<String, User> users = new HashMap<>();

	public AuthService() {
		User admin = new User (
			"admin",
			passwordEncoder.encode("admin123"),
			"ADMIN"
		);
		users.put("admin", admin);
	}

	//Authenticating user
	public User authenticate( String username, String password) {
		User user = users.get(username);

		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			return user;
		}
		return null;
	}

	//Get user by username
	public User getUser(String username) {
		return users.get(username);
	}

	//Changin password
	public boolean changePassword( String username, String oldPassword, String newPassword) {
		User user = authenticate(username, oldPassword);
		if (user != null) {
			user.setPassword(passwordEncoder.encode(newPassword));
			return true;
		}
		return false;
	}

}
