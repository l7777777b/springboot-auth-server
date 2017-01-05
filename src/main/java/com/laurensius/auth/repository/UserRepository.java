package com.laurensius.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laurensius.auth.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findOneByEmail(String email);
}