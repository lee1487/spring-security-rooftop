package com.hys.module.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hys.module.vo.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);

}
