package com.seazon.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seazon.board.domain.SignUp;

public interface SignUpRepository extends JpaRepository<SignUp, Integer> {
	SignUp findBySubject(String subject);   // findBySubject
	List<SignUp> findBySubjectLike(String subject); // findBySubjectLike(특정문자열 포함)
	Page<SignUp> findAll(Pageable pageable); // 페이징 구현하기
	Page<SignUp> findAll(Specification<SignUp> spec, Pageable pageable);  // 검색기능
	
	@Modifying
	@Query("UPDATE SignUp q SET q.view = q.view + 1 WHERE q.id = :id")
	void incrementViewCount(@Param("id") int id);                             // 추천레시피
}