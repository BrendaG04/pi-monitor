package com.brenda.pimonitor.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.brenda.pimonitor.model.UserEntity;
import com.brenda.pimonitor.repository.UserRepository;
import com.brenda.pimonitor.validation.PasswordValidator;

/**
	Spring Security ~ Auth service class / handles login logic
*/

@Service
public class AuthService {

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private PasswordValidator passwordValidator;

	@Autowired
	private UserRepository userRepository;

	public AuthService() {
	}

    	// Initialize default admin user if not exists
    	@jakarta.annotation.PostConstruct
    	public void init() {
        	if (!userRepository.existsByUsername("admin")) {
           		UserEntity admin = new UserEntity(
                		"admin",
                		passwordEncoder.encode("Admin123!"),
                		"admin@pimonitor.local",
                		"ADMIN"
            		);
            		admin.setApproved(true);  // Auto-approve admin
            		admin.setApprovedAt(LocalDateTime.now());
            		admin.setApprovedBy("SYSTEM");
            		userRepository.save(admin);
            		System.out.println("✅ Default admin user created");
        	}

		//Default user
		if (!userRepository.existsByUsername("guest")) {
			UserEntity guest = new UserEntity(
				"guest",
				passwordEncoder.encode("Guest123!"),
				"guest@pimonitor.local",
				"USER"
			);
			guest.setApproved(true);
			guest.setApprovedAt(LocalDateTime.now());
			guest.setApprovedBy("SYSTEM");
			userRepository.save(guest);
			System.out.println("✅ Default guest user created");
		}
    	}

	//Authenticating user
	public UserEntity authenticate(String username, String password) {
		Optional<UserEntity> userOpt = userRepository.findByUsername(username);

		if (userOpt.isPresent()){
			UserEntity user = userOpt.get();

			if (!user.isApproved()){
				return null;
			}

			if (passwordEncoder.matches(password, user.getPassword())) {
				return user;
			}
		}
		return null;
	}

	//Get user by username
	public UserEntity getUser(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	//Validate and creates new user
	public PasswordValidator.ValidationResult registerUser(String username, String password, String email) {
		//validates pass strength
		PasswordValidator.ValidationResult validation = passwordValidator.validate(password);

		if (!validation.isValid()){
			return validation;
		}

		//checks if usrname exists already
		if (userRepository.existsByUsername(username)){
			return new PasswordValidator.ValidationResult(
				false,
				List.of("Username already exists!")
			);
		}

		//checks if email already exists
		if (userRepository.existsByEmail(email)) {
			return new PasswordValidator.ValidationResult(
				false,
				List.of("Email already exists!")
			);
		}

		//creates new user
		UserEntity newUser = new UserEntity (
			username,
			passwordEncoder.encode(password),
			email,
			"USER"
		);
		newUser.setApproved(true);
		newUser.setApprovedAt(LocalDateTime.now());
		newUser.setApprovedBy("Auto");
		userRepository.save(newUser);
		return new PasswordValidator.ValidationResult(true, List.of());
	}

	//Changin password
	public boolean changePassword( String username, String oldPassword, String newPassword) {
		PasswordValidator.ValidationResult validation = passwordValidator.validate(newPassword);

		if (!validation.isValid()) {
			return false;
		}

		UserEntity user = authenticate(username, oldPassword);
		if (user != null) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return true;
		}
		return false;
	}
}
