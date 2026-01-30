package com.brenda.pimonitor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
	User Repository ~Interacts with databse  / DAO
*/

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    // Find user by username
    Optional<UserEntity> findByUsername(String username);
    
    // Find all pending users (waiting for approval)
    List<UserEntity> findByApprovedFalse();
    
    // Find all approved users
    List<UserEntity> findByApprovedTrue();
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Check if email exists
    boolean existsByEmail(String email);
}
