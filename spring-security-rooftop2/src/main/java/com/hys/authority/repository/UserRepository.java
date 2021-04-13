package com.hys.authority.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hys.authority.vo.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);

}
