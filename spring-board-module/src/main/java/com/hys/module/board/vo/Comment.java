package com.hys.module.board.vo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hys.module.user.vo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "COMMENT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String comment;
	
	@CreationTimestamp
	private LocalDateTime created;
	
	@ManyToOne
	@JoinColumn(name="boardId")
	private Board board;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
}
