package com.hys.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hys.jwt.vo.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);

}
