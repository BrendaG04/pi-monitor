package com.brenda.pimonitor.dto;


/**
	Spring Security - SignupResponse ~ DTOs Inner class for signup response
*/
public class SignupResponse {
        
	private String message;
        private String username;
        
        public SignupResponse(String message, String username) {
            this.message = message;
            this.username = username;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
    }
