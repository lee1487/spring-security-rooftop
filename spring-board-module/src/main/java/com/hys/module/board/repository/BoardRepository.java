package com.hys.module.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hys.module.board.vo.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{

	Page<Board> findAllByOpenOrderByCreatedDesc(boolean open, Pageable pageable);
}
