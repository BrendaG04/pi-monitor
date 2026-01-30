package com.brenda.pimonitor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
	Spring Security ~ Auth service class / handles login logic
*/

@Service
public class AuthService {

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private PasswordValidator passwordValidator;

	//No database for now, im using in memory user storage ~ map
	private final Map<String, User> users = new HashMap<>();

	public AuthService() {
		User admin = new User (
			"admin",
			passwordEncoder.encode("Admin123!"),
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

	//Validate and creates new user
	public PasswordValidator.ValidationResult createUser(String username, String password, String role) {
		//validates pass strength
		PasswordValidator.ValidationResult validation = passwordValidator.validate(password);

		if (!validation.isValid()){
			return validation;
		}

		//checks if usrname exists already
		if (users.containsKey(username)){
			return new PasswordValidator.ValidationResult(
				false,
				java.util.List.of("Username already exists!")
			);
		}

		//creates new user
		User newUser = new User (
			username,
			passwordEncoder.encode(password),
			role
		);
		users.put(username, newUser);

		return new PasswordValidator.ValidationResult(true, java.util.List.of());
	}



	//Changin password
	public boolean changePassword( String username, String oldPassword, String newPassword) {
		PasswordValidator.ValidationResult validation = passwordValidator.validate(newPassword);

		if (!validation.isValid()) {
			return false;
		}

		User user = authenticate(username, oldPassword);
		if (user != null) {
			user.setPassword(passwordEncoder.encode(newPassword));
			return true;
		}
		return false;
	}

}


