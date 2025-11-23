package com.seazon.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seazon.board.domain.Travel;

public interface TravelRepository extends JpaRepository<Travel, Integer> {
	Travel findBySubject(String subject);   // findBySubject
	List<Travel> findBySubjectLike(String subject); // findBySubjectLike(특정문자열 포함)
	Page<Travel> findAll(Pageable pageable); // 페이징 구현하기
	Page<Travel> findAll(Specification<Travel> spec, Pageable pageable);  // 검색기능
	
	@Modifying
	@Query("UPDATE Travel q SET q.view = q.view + 1 WHERE q.id = :id")
	void incrementViewCount(@Param("id") int id);        
	
//	@Query("SELECT t FROM Travel t WHERE t.travelInfo_day LIKE %:month%")
//	    List<Travel> findByMonth(@Param("month") String month);// 추천축제
	
	@Query("SELECT t FROM Travel t WHERE SUBSTRING(t.travelInfo_day, 1, 4) = :year AND SUBSTRING(t.travelInfo_day, 6, 2) = :month")
	List<Travel> findByYearAndMonth(@Param("year") String year,
	                                @Param("month") String month);
	
	@Query("SELECT t FROM Travel t WHERE substring(t.travelInfo_day, 6, 2) = :month")
	List<Travel> findByMonth(@Param("month") String month);
}