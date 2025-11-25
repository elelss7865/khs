package com.seazon.board.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.seazon.board.domain.Answer;
import com.seazon.board.domain.Travel;
import com.seazon.board.domain.SiteUser;
import com.seazon.board.repository.TravelRepository;
import com.seazon.board.util.DataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TravelService {

   private final TravelRepository travelRepository;
   
   // 검색 기능 (검색값 : kw)
      // Specification => 여러 테이블에서 데이터를 검색해야 할 경우에 JPA가 제공하는 인터페이스
      private Specification<Travel> search(String kw) {
          return new Specification<>() {
            private static final long serialVersionUID = 1L;
            
            @Override  
            public Predicate toPredicate(Root<Travel> r, CriteriaQuery<?> query, CriteriaBuilder cb) {
            	
                  // r : 기준을 의미하는 travel
               query.distinct(true);  // 중복을 제거
               
               Join<Travel, SiteUser> u1 = r.join("author", JoinType.LEFT); 
                   // u1 : travel엔티티와 SiteUser 엔티티를 아우터 조인 하여 만든 SiteUser 엔티티의 객체
               
               Join<Travel, Answer> a = r.join("answerList", JoinType.LEFT);
               // a : travel 엔티티와 Answer 엔티티를 아우터 조인하여 만든 Answer 엔티티의 객체  
               
               Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                    // u2 : a 와 다시한번 SiteUser 엔티티와 아우터 조인하여 SiteUser 엔티티의 객체(답변 작성자를 검색하기 위해서 필요)
               
               return cb.or(
            		   cb.like(r.get("subject"), "%" + kw + "%"),
            		    cb.like(r.get("content"), "%" + kw + "%"),
            		    cb.like(u1.get("username"), "%" + kw + "%"),
            		    cb.like(a.get("content"), "%" + kw + "%"),
            		    cb.like(u2.get("username"), "%" + kw + "%"),
            		    cb.like(r.get("category"), "%" + kw + "%"),
            		    cb.like(r.get("travelInfo_place"), "%" + kw + "%"),
            		    cb.like(r.get("place"), "%" + kw + "%"),
            		    cb.like(r.get("travelInfo_day"), "%" + kw + "%"));
               }
           };
       }
      
      // 모든 엔티티 검색
      public List<Travel> getList() {
         return this.travelRepository.findAll();
      }
      
      public Travel getTravel(Integer id) {
         Optional<Travel> travel = this.travelRepository.findById(id);
         if (travel.isPresent()) {
            return travel.get();
         } else {
            throw new DataNotFoundException("travel not found");
         }
      }
      
      // 축제 저장 기능
      public void create(String subject,SiteUser user,MultipartFile file,String travelIntro,
    	        String category,String place,String travelInfo_day,String travelInfo_place,String travelInfo_pay,
    	        String content,String travelInfo_phone,String travelInfo_organizer,String travelInfo_homepage) throws Exception {
	
		// 썸네일 저장
		String projectPath = "D:\\kim\\boot\\files";
		UUID uuid = UUID.randomUUID();
		String fileName = uuid + "_" + file.getOriginalFilename();
		File saveFile = new File(projectPath, fileName);
		file.transferTo(saveFile);
		
	     Travel r = new Travel();
	     
	     // String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
	     Travel travel = new Travel();
	     r.setFileName(fileName);
	     r.setFilePath("/files/" + fileName);
	     r.setSubject(subject);
	     r.setCreateDate(LocalDateTime.now());
	     r.setTravelIntro(travelIntro);
	     r.setCategory(category);
	     r.setPlace(place);
	     r.setTravelInfo(travelInfo_day + travelInfo_place + travelInfo_pay);
	     r.setTravelInfo_day(travelInfo_day);
	     
	  	    // 날짜 처리 로직 추가 👇
	         if (travelInfo_day != null && travelInfo_day.contains("~")) {
	             // 1. "~" 기준으로 문자열 분리
	             String[] dates = travelInfo_day.split("~"); 
	             
	             if (dates.length == 2) {
	                 // 2. 공백 제거 후 시작일/종료일 추출
	                 String startDate = dates[0].trim();
	                 String endDate = dates[1].trim();

	                 // 3. 엔티티에 설정 
	                 r.setTravel_start_date(startDate);
	                 r.setTravel_end_date(endDate);
	             }
	         }
	         
	     r.setTravelInfo_day(travelInfo_day);
	     r.setTravelInfo_place(travelInfo_place);
	     r.setTravelInfo_pay(travelInfo_pay);
	     r.setTravelInfo_phone(travelInfo_phone);
	     r.setTravelInfo_organizer(travelInfo_organizer);
	     r.setTravelInfo_homepage(travelInfo_homepage);
	     r.setContent(content);
//	     r.setContentFilePath("/files/contents/" + contentFileName);
	     r.setAuthor(user);
	           
	       this.travelRepository.save(r);
	       
	       log.info("로그create" + r);
      }
      		

      // 페이징 구현 기능(검색 기능 추가)
      public Page<Travel> getList(int page, String kw) {
         List<Sort.Order> sorts = new ArrayList<>();
         sorts.add(Sort.Order.desc("createDate"));
         Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
         Specification<Travel> spec = search(kw);
         return this.travelRepository.findAll(spec, pageable);
      }
      
      // 최근 게시물
      public Page<Travel> getRecentlyList(int page, String kw) {
         List<Sort.Order> sorts = new ArrayList<>();
         sorts.add(Sort.Order.desc("createDate"));
         Pageable pageable = PageRequest.of(page, 4, Sort.by(sorts));
         Specification<Travel> spec = search(kw);
         return this.travelRepository.findAll(spec, pageable);
      }
      
      // 조회수 많은 게시물
      public Page<Travel> getTopList(int page, String kw) {
         List<Sort.Order> sorts = new ArrayList<>();
         sorts.add(Sort.Order.desc("view"));
         Pageable pageable = PageRequest.of(page, 4, Sort.by(sorts));
         Specification<Travel> spec = search(kw);
         return this.travelRepository.findAll(spec, pageable);
      }
      
     // 모든 게시물(section)
      public Page<Travel> getAllList(int page, String kw) {
          List<Sort.Order> sorts = new ArrayList<>();
          sorts.add(Sort.Order.desc("createDate"));
          int pageSize = 20;

          Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
          Specification<Travel> spec = search(kw);

          return this.travelRepository.findAll(spec, pageable);
      }
      
      /**
       * 카테고리 값을 기반으로 travel 데이터를 조회합니다.
       * @param category 조회할 카테고리 문자열 (예: "먹거리", "힐링")
       * @return 해당 카테고리에 속하는 Travel 엔티티 리스트
       */
      public List<Travel> getListByCategory(String category) {
          if (category == null || category.isEmpty()) {
              // 카테고리가 null이거나 비어있으면 전체 목록 반환
              return this.travelRepository.findAll();
          }
          // Repository에서 카테고리별로 조회
          return this.travelRepository.findByCategory(category);
      }
      
      public List<Travel> getListByPlace(String place) {
    	  if (place == null || place.isEmpty()) {
    		  // 카테고리가 null이거나 비어있으면 전체 목록 반환
    		  return this.travelRepository.findAll();
    	  }
    	  // Repository에서 카테고리별로 조회
    	  return this.travelRepository.findByPlace(place);
      }
      
      
      // 질문 수정 기능
      public void modify(Travel travel, String subject, MultipartFile file, String travelIntro, String category,
  			String travelInfo_day, String travelInfo_place, String travelInfo_pay) throws Exception{
         String projectPath = "D:\\kim\\boot\\files";
           UUID uuid = UUID.randomUUID();
           String fileName = uuid + "_" + file.getOriginalFilename();
           String filePath = "/files/" + fileName;
           File saveFile = new File(projectPath, fileName);
           file.transferTo(saveFile);
           travel.setFileName(fileName);
           travel.setFilePath(filePath);
           travel.setSubject(subject);
//    	   travel.setContent(content);
    	   travel.setTravelIntro(travelIntro);
    	   travel.setCategory(category);
    	   travel.setTravelInfo(travelInfo_day + travelInfo_place + travelInfo_pay);
    	   travel.setTravelInfo_day(travelInfo_day);
    	   travel.setTravelInfo_place(travelInfo_place);
    	   travel.setTravelInfo_pay(travelInfo_pay);
    	   travel.setModifyDate(LocalDateTime.now());

    	   this.travelRepository.save(travel);
      }
      
      // 질문 삭제 기능
      public void delete(Travel travel) {
         this.travelRepository.delete(travel);
      }
      
      // 추천
      public void vote(Travel travel, SiteUser siteuser) {
         travel.getVoter().add(siteuser);
         this.travelRepository.save(travel);
      }
      
      // 추천축제
      @Transactional
      public void incrementViewCount(int id) {
          travelRepository.incrementViewCount(id);
      }
      
      // 전체 축제 수
      public long getTotalCount() {
    	    return travelRepository.count();
      }
      
      public List<Travel> getListByFilter(String category, String place) {
    	    
    	    // 1) 필터 없음 → 전체
    	    if ((category == null || category.isEmpty()) &&
    	        (place == null || place.isEmpty())) {
    	        return this.travelRepository.findAll();
    	    }

    	    // 2) category만
    	    if (place == null || place.isEmpty()) {
    	        return this.travelRepository.findByCategory(category);
    	    }

    	    // 3) place만
    	    if (category == null || category.isEmpty()) {
    	        return this.travelRepository.findByPlace(place);
    	    }

    	    // 4) category + place 모두 적용
    	    return this.travelRepository.findByCategoryAndPlace(category, place);
    	}

}