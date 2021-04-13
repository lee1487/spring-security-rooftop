package com.hys.blog.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "MEMBER")
public class Member {
	
	@JsonIgnore
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
	@SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ", allocationSize = 1)
	private Long id;
	
	@Column(name ="USERNAME", length = 50, unique = true)
	private String username;
	
	@JsonIgnore
	@Column(name = "PASSWORD", length = 100)
	private String password;
	
	@Column(name = "EMAIL", length = 50, unique = true)
	private String email;
	
	@Column(name = "ROLE", length = 50)
	private String role;
}
