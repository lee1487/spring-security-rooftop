package com.hys.blog.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hys.blog.core.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByEmail(String email);
}
