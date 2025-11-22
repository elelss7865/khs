package com.seazon.board.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Entity
@Data
public class Travel {
	 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 축제의 고유 번호 - 기본키(primary key)
	
	@Column(length = 200)
	private String subject;  // 축제 제목
	
	@Column(name = "content")
	private String content;  // 축제 내용
	
	private LocalDateTime createDate;  // 축제 최초 작성 일시
	
	@OneToMany(mappedBy = "travel", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
	
	@ManyToOne
	private SiteUser author;  // 축제에 author 속성을 추가(엔티티 변경)
	
	private LocalDateTime modifyDate;   // 축제 수정 일시
	
	@ManyToMany  // N:N
	Set<SiteUser> voter;     // 추천인(voter) 속성 추가
	                         // Set은 중복을 허용하지 않기 떄문에 사용
	
	private String fileName; // 썸네일 파일 이름
	
	private String filePath; // 썸네일 파일 주소
	
	@Column(columnDefinition = "integer default 0", nullable = false)
	private Integer view = 0; // 조회수

	private String travelIntro;     // 축제 정보	

	private String travelInfo;      // 축제 내용
	
	private String category;      // 카테고리
    
	private String travelInfo_day;     // 축제 일정

	private String travelInfo_place;     // 축제 장소

	private String travelInfo_pay;     // 축제 비용
	
	private String sns;
	
	// 축제 관련 전화번호
	private String travelInfo_phone;    
	
	// 축제 주최
	private String travelInfo_organizer;     
	
	// 축제 공식홈페이지
	private String travelInfo_homepage;  
	
	private String contentFilePath;    // 축제 이미지
	
}
