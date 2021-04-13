package com.hys.module.board.service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hys.module.board.repository.BoardRepository;
import com.hys.module.board.repository.CommentRepository;
import com.hys.module.board.vo.Board;
import com.hys.module.board.vo.Comment;
import com.hys.module.board.vo.dto.BoardSummary;
import com.hys.module.user.service.UserService;
import com.hys.module.user.vo.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;
	
	private final UserService userService;
	
	// 1. 저장
	@Transactional
	public Board save(Board board) {
		return boardRepository.save(board);
	}
	
	// 2. 리스트 페이징
	@Transactional
	public Page<BoardSummary> list(int pageNum, int size) {
		Page<Board> boardPage = boardRepository.findAllByOpenOrderByCreatedDesc(true, 
				PageRequest.of(pageNum-1, size));
		 
		Map<Long, User> userMap = userService.getUserMap(boardPage.getContent().stream().map(
				board -> board.getUser().getId()).collect(Collectors.toSet()));
		return boardPage.map(board -> BoardSummary.of(board, userMap.get(board.getUser().getId())));
	}
	
	// 3. 수정
	// 컨텐츠, 타이틀, 컨텐츠-타이틀
	// JPA 더티체킹
	@Transactional
	private boolean update(Long boardId, String content, String title) {
		Board board = boardRepository.findById(boardId)
					.orElseThrow(() -> {
						return new IllegalArgumentException("글 찾기 실패");
					});
		
		board.setTitle(title);
		board.setContent(content);
		
		return true;
		
	}
	
	@Transactional
	private boolean updateContent(Long boardId, String content) {
		Board board = boardRepository.findById(boardId)
					.orElseThrow(() -> {
						return new IllegalArgumentException("글 찾기 실패");
					});
		
		board.setContent(content);
		
		return true;
		
	}
	
	@Transactional
	private boolean updateTitle(Long boardId, String title) {
		Board board = boardRepository.findById(boardId)
					.orElseThrow(() -> {
						return new IllegalArgumentException("글 찾기 실패");
					});
		
		board.setTitle(title);
		
		return true;
		
	}
	
	// 4. 가져오기
	public Optional<Board> findBoard(Long boardId) {
		return boardRepository.findById(boardId);
	}
	
	// 5. 삭제 
	public Optional<Board> removeBoard(Long boardId) {
		return findBoard(boardId).map(board -> {
			boardRepository.delete(board);
			return board;
		});
	}
	
	// 6. 코멘트 추가 
	public Comment addComment(Long boardId, Comment comment) {
		return commentRepository.mSave(boardId, comment.getUser().getId(), comment.getComment());
	}
	
	// 7. 리스트 클리어 (테스트용)
}
