package com.hys.module.board.vo.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.hys.module.board.vo.Board;
import com.hys.module.board.vo.Comment;
import com.hys.module.user.vo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardSummary {

	private Long id;
	private String title;
	
	private User user;
	private int commentCount;
	
	@CreationTimestamp
	private LocalDateTime created;
	
	@UpdateTimestamp
	private LocalDateTime updated;
	
	public static BoardSummary of(Board board, User user) {
		return BoardSummary.builder()
				.id(board.getId())
				.title(board.getTitle())
				.user(user)
				.commentCount(board.getComments() == null ? 0 : board.getComments().size())
				.created(board.getCreated())
				.updated(board.getUpdated())
				.build();
	}
}
