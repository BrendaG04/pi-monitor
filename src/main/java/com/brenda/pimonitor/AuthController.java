package com.brenda.pimonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
	Spring Security ~ Auth Controller / handles the /login endpoint
*/

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/login")
	private ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

		//Authenticating user
		User user = authService.authenticate(
			loginRequest.getUsername(),
			loginRequest.getPassword()
		);

		if (user != null ) {
			String token = jwtUtil.generateToken(user.getUsername());

			LoginResponse response = new LoginResponse(
				token,
				user.getUsername(),
				"Login successful"
			);
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body("Invalid username or password");
		}
	}

	@GetMapping("/verify")
	public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.substring(7);
			String username = jwtUtil.extractUsername(token);

			if (jwtUtil.validateToken(token, username)) {
				return ResponseEntity.ok("Token is Valid");
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalide token ");
		}
	}
}
