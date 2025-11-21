package com.seazon.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seazon.board.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer>{

}
