package com.hys.module.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hys.module.board.vo.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	@Modifying
	@Query(value="INSERT INTO COMMENT(userId, boardId, comment, created) VALUES(?1, ?2, ?3, now())", nativeQuery = true)
	Comment mSave(Long userId, Long boardId, String comment);
	
}
