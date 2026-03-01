package com.brenda.pimonitor.controller;

import com.brenda.pimonitor.dto.LoginRequest;
import com.brenda.pimonitor.dto.LoginResponse;
import com.brenda.pimonitor.dto.SignupRequest;
import com.brenda.pimonitor.dto.SignupResponse;
import com.brenda.pimonitor.model.UserEntity;
import com.brenda.pimonitor.security.JwtUtil;
import com.brenda.pimonitor.service.AuthService;
import com.brenda.pimonitor.service.RateLimitService;
import com.brenda.pimonitor.validation.PasswordValidator;
import jakarta.validation.Valid;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
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

	@Autowired
	private RateLimitService rateLimitService;

	@PostMapping("/login")
	private ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

		//getting clients Ip address
		String clientIP = getClientIP(request);

		//checks rate limit
		Bucket bucket = rateLimitService.resolveBucket(clientIP);
		ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

		if (!probe.isConsumed()) {
			//rate limit exceeded
			long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
					.body("Too many login attempts.Please try again in " +  waitForRefill + " seconds.");
		}

		//Authenticating user
		UserEntity user = authService.authenticate(
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

	@PostMapping("/signup")
    	public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {

	        // Register user (creates pending user)
        	PasswordValidator.ValidationResult result = authService.registerUser(
        	    signupRequest.getUsername(),
            	    signupRequest.getPassword(),
            	    signupRequest.getEmail()
        	);
        
        	if (result.isValid()) {
        	    return ResponseEntity.ok(new SignupResponse(
			"Registration succesful! You can now login.",
                	signupRequest.getUsername()
            	    ));
        	} else {
            		return ResponseEntity.badRequest().body(new SignupResponse(
                		result.getErrorMessage(),
                		null
            		));
        	}
    	}

	//helper method to get clients ip
	private String getClientIP(HttpServletRequest request) {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
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
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
	}
}
